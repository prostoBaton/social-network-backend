package com.example.securityservice.model;

import com.example.securityservice.dto.ResponseDto;
import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "isEnabled")
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "subs", joinColumns = @JoinColumn(name="subscribers"), inverseJoinColumns = @JoinColumn(name = "authors"))
    private Set<User> subscriptions; //TODO cascade

    @ManyToMany(mappedBy = "subscriptions")
    private Set<User> subscribers;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Set<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<User> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    public List<ResponseDto> getSubscribersDto() {
        List<ResponseDto> response = new ArrayList<>();
        for (User user : subscribers) {
            ResponseDto responseDto = new ResponseDto(user.getId(),user.getUsername());
            response.add(responseDto);
        }
        return response;
    }

    public List<ResponseDto> getSubscriptionsDto() {
        List<ResponseDto> response = new ArrayList<>();
        for (User user : subscriptions) {
            ResponseDto responseDto = new ResponseDto(user.getId(),user.getUsername());
            response.add(responseDto);
        }
        return response;
    }
}
