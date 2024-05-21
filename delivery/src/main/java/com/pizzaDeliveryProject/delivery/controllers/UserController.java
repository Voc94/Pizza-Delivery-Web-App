package com.pizzaDeliveryProject.delivery.controllers;

import com.pizzaDeliveryProject.delivery.config.JwtService;
import com.pizzaDeliveryProject.delivery.dto.UserDTO;
import com.pizzaDeliveryProject.delivery.service.AdminService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AdminService adminService;
    private final JwtService jwtService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public UserController(AdminService adminService, JwtService jwtService, SimpMessagingTemplate messagingTemplate) {
        this.adminService = adminService;
        this.jwtService = jwtService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/requestRestaurant")
    public ResponseEntity<String> requestRestaurant(@RequestParam Long userId) {
        // Logic to process the request
        messagingTemplate.convertAndSendToUser(userId.toString(), "/topic/notifications", "Restaurant request processed");
        return ResponseEntity.ok("Restaurant request sent successfully.");
    }



}

