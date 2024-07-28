package com.example.securityservice.controller;

import com.example.securityservice.dto.ResponseDto;
import com.example.securityservice.model.User;
import com.example.securityservice.service.JwtService;
import com.example.securityservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class SubscribeController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public SubscribeController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<ResponseDto> findAll(){
        return userService.findAll();
    }

    @GetMapping("/subscribers")
    public List<ResponseDto> getSubscribers(@RequestHeader("Authorization") String authHeader){
        User user = userService.findByUsername(jwtService.extractUsername(authHeader.substring(7))).orElseThrow(EntityNotFoundException::new);
        return user.getSubscribersDto();
    }

    @GetMapping("/subscriptions")
    public List<ResponseDto> getSubscriptions(@RequestHeader("Authorization") String authHeader){
        User user = userService.findByUsername(jwtService.extractUsername(authHeader.substring(7))).orElseThrow(EntityNotFoundException::new);
        return user.getSubscriptionsDto();
    }

    @PatchMapping("/subscribe/{id}")
    public String subscribe(@PathVariable int id,
                            @RequestHeader("Authorization") String authHeader){
        User subscriber = userService.findByUsername(jwtService.extractUsername(authHeader.substring(7))).orElseThrow(EntityNotFoundException::new);
        return userService.subscribe(id,subscriber);
    }

    @PatchMapping("/unsubscribe/{id}")
    public String unsubscribe(@PathVariable int id,
                              @RequestHeader("Authorization") String authHeader){
        User subscriber = userService.findByUsername(jwtService.extractUsername(authHeader.substring(7))).orElseThrow(EntityNotFoundException::new);
        return userService.unsubscribe(id,subscriber);
    }
}
