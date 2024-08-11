package com.example.securityservice.controller;

import com.example.securityservice.dto.RequestDto;
import com.example.securityservice.model.User;
import com.example.securityservice.service.JwtService;
import com.example.securityservice.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RequestDto requestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return bindingResult.getFieldError().getDefaultMessage();
        User user = new User(requestDto.getUsername(), requestDto.getPassword(), requestDto.getEmail());
        return userService.save(user);
    }

    @GetMapping("/activate")
    public String activate(@RequestParam String code){
        return userService.activate(code);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestHeader("Authorization") String authHeader){
        return userService.delete(userService.findByUsername(jwtService.extractUsername(authHeader)).orElseThrow(()->new EntityNotFoundException("User not found")));
    }

    @PostMapping("/token")
    public String getToken(@Valid @RequestBody RequestDto requestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return bindingResult.getFieldError().getDefaultMessage();
        User user = userService.findByUsername(requestDto.getUsername()).orElseThrow(EntityNotFoundException::new);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDto.getUsername(),
                requestDto.getPassword()));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user);
        if (!user.isEnabled())
            return "Your account is not activated";
        return "Invalid username or password";
    }

    @GetMapping("/validate")
    public String isTokenValid(@RequestParam("token") String token){
        try {
            User user = userService.findByUsername(jwtService.extractUsername(token)).orElseThrow(EntityNotFoundException::new);
            if (jwtService.isTokenValid(token,user))
                return "Token is valid";
        } catch (ExpiredJwtException e){}
        return "Invalid token";

    }

    @GetMapping("/id")
    public String getIdByToken(@RequestParam("token") String token){
        return String.valueOf(userService.findByUsername(jwtService.extractUsername(token.substring(7))).orElseThrow(()-> new EntityNotFoundException("User not found")).getId());
    }

    @GetMapping("/username")
    public String getUsernameByToken(@RequestParam("token") String token){
        return jwtService.extractUsername(token.substring(7));
    }
    @GetMapping("/subs")
    public List<String> getSubscribersByToken(@RequestParam("token") String token){
        List<String> emails = new ArrayList<>();
        Set<User> subscribers = userService.findByUsername(jwtService.extractUsername(token.substring(7))).orElseThrow(()-> new EntityNotFoundException("User not found")).getSubscribers();
        for (User subscriber : subscribers) {
            emails.add(subscriber.getEmail());
        }
        return emails;
    }

}
