package com.example.securityservice.service;

import com.example.securityservice.dto.RabbitDto;
import com.example.securityservice.dto.ResponseDto;
import com.example.securityservice.model.Activation;
import com.example.securityservice.model.User;
import com.example.securityservice.repository.ActivationRepository;
import com.example.securityservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ActivationRepository activationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public UserService(UserRepository userRepository, ActivationRepository activationRepository, PasswordEncoder passwordEncoder, AmqpTemplate amqpTemplate) {
        this.userRepository = userRepository;
        this.activationRepository = activationRepository;
        this.passwordEncoder = passwordEncoder;
        this.amqpTemplate = amqpTemplate;
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<ResponseDto> findAll(){
        List<User> users = userRepository.findAll();
        List<ResponseDto> response = new ArrayList<>();
        for (User user : users) {
            ResponseDto responseDto = new ResponseDto(user.getId(),user.getUsername());
            response.add(responseDto);
        }
        return response;
    }

    @Transactional
    public String save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Activation activation = new Activation();
        activation.setUserId(findByUsername(user.getUsername()).get().getId());
        activation.setCode(UUID.randomUUID().toString().replaceAll("_", "1").replaceAll("-","0"));
        activation.setExpirationTime(LocalDateTime.now().plusHours(2));
        activationRepository.save(activation);

        RabbitDto rabbitDto = new RabbitDto(user.getEmail(), "Your activation code is " + activation.getCode());
        amqpTemplate.convertAndSend("activations_exchange","activations_routing", rabbitDto);

        return "User has been registered";
    }

    @Transactional
    public String delete(User user) {
        for (User sub : user.getSubscriptions()) {
            sub.getSubscribers().remove(user);
        }
        for (User sub : user.getSubscribers()) {
            sub.getSubscriptions().remove(user);
        }
        userRepository.delete(user);
        return "User has been deleted";
    }

    @Transactional
    public String subscribe(int id, User subscriber) {
        User author = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found"));
        Set<User> subs = author.getSubscribers();
        subs.add(subscriber);
        userRepository.save(author);
        return "You have subscribed";
    }

    @Transactional
    public String unsubscribe(int id, User subscriber) {
        User author = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User not found"));
        Set<User> subs = author.getSubscribers();
        subs.remove(subscriber);
        userRepository.save(author);
        return "You have unsubscribed";
    }

    @Transactional
    public String activate(String code) {
        System.out.println(code);
        Activation activation = activationRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Invalid code"));
        if (activation.getExpirationTime().isBefore(LocalDateTime.now())){
            activationRepository.delete(activation);
            return "Code has expired";
        }
        User user = userRepository.findById(activation.getUserId()).get();
        System.out.println(user.getUsername());
        user.setEnabled(true);
        userRepository.save(user);
        activationRepository.delete(activation);
        return "User has been activated";
    }
}
