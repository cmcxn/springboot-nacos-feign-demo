package com.example.consumer.controller;

import com.example.api.model.User;
import com.example.consumer.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer/users")
@RequiredArgsConstructor
public class UserConsumerController {
    
    private final UserServiceClient userServiceClient;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userServiceClient.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userServiceClient.getUserById(id);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userServiceClient.createUser(user);
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userServiceClient.updateUser(id, user);
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userServiceClient.deleteUser(id);
    }
}