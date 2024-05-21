package com.pizzaDeliveryProject.delivery.auth;

import com.pizzaDeliveryProject.delivery.config.JwtService;
import com.pizzaDeliveryProject.delivery.exceptions.ApiExceptionsResponse;
import com.pizzaDeliveryProject.delivery.mappers.UserMapper;
import com.pizzaDeliveryProject.delivery.models.user.Role;
import com.pizzaDeliveryProject.delivery.repository.UserRepository;
import com.pizzaDeliveryProject.delivery.models.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final Validator validator;
    public AuthenticationResponse register(RegisterRequest request) {
        if (request == null) return null;
        User user = new User();
        user.setId(null);
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRestaurant(null);
        user.setRole(Role.USER);
        // Perform validation
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            String violationMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ApiExceptionsResponse("Validation failed: " + violationMessages, HttpStatus.BAD_REQUEST, List.of(violationMessages));
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ApiExceptionsResponse("Email is already in use", HttpStatus.BAD_REQUEST, List.of("Email already exists"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user); // Ensure `generateToken` is properly defined to accept `UserDetails`
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .role("ROLE_" + user.getRole().name())
                .build();
    }
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtService.blacklistToken(token);  // Call to JwtService to blacklist the token
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
