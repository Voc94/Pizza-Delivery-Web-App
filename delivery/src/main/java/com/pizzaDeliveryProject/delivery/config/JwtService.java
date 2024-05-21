package com.pizzaDeliveryProject.delivery.config;

import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    UserRepository userRepository;
    private static final String SECRET_KEY = "593d3e5176502c34644c526c5925572679614f47217231573c623d402333533f";
    @Autowired
    private JedisPool jedisPool;

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof User)) {
            throw new IllegalArgumentException("UserDetails must be an instance of User");
        }
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("email", user.getEmail());
        return generateTokenWithClaims(claims, user.getEmail());
    }

    private String generateTokenWithClaims(Map<String, Object> claims, String email) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(token, 86400, email);
        } catch (Exception e) {
            logger.error("Error interacting with Redis", e);
        }
        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (userDetails instanceof User && email.equals(((User) userDetails).getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public long getActiveUsersCount() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys("*"); // Get all keys in Redis
            long activeUsersCount = keys.stream()
                    .map(key -> jedis.get(key)) // Get the username associated with each token
                    .filter(Objects::nonNull) // Filter out null values (expired tokens)
                    .distinct() // Count unique usernames
                    .count();
            return activeUsersCount;
        } catch (Exception e) {
            logger.error("Error getting active users count from Redis", e);
            return -1; // Return -1 to indicate error
        }
    }

    public void blacklistToken(String token) {
        try (Jedis jedis = jedisPool.getResource()) {
            long ttl = jedis.ttl(token);
            if (ttl > 0) {
                jedis.setex("blacklisted:" + token, ttl, "true");
            }
        } catch (Exception e) {
            logger.error("Failed to blacklist token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }

}
