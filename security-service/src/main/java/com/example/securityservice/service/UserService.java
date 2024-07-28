package com.example.securityservice.service;

import com.example.securityservice.dto.ResponseDto;
import com.example.securityservice.model.User;
import com.example.securityservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

    }

    @Transactional
    public String delete(User user) {
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
}
