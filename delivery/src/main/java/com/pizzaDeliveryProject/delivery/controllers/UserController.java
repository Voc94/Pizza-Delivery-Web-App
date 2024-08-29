package com.pizzaDeliveryProject.delivery.controllers;

import com.pizzaDeliveryProject.delivery.config.JwtService;
import com.pizzaDeliveryProject.delivery.dto.ClientDTO;
import com.pizzaDeliveryProject.delivery.dto.UserDTO;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.service.AdminService;
import com.pizzaDeliveryProject.delivery.service.UserService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;

    @PostMapping("/requestRestaurant")
    public ResponseEntity<String> requestRestaurant(@RequestParam Long userId) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/notifications", "Restaurant request processed");
        return ResponseEntity.ok("Restaurant request sent successfully.");
    }
    @GetMapping("/profile")
    public ResponseEntity<ClientDTO> getUserProfile(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.substring(7));
        User user = userService.findByEmail(email);
        ClientDTO clientDTO = new ClientDTO(user.getUsername(), user.getEmail(), user.getRole().toString());
        return ResponseEntity.ok(clientDTO);
    }
    @GetMapping("/email")
    public ResponseEntity<Long> getUserId(@RequestHeader("Authorization") String token){
        String email = jwtService.extractEmail((token.substring(7)));
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user.getId());
    }


}

