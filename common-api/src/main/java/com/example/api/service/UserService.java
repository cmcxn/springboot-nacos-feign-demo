package com.example.api.service;

import com.example.api.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserService {
    
    @GetMapping("/users")
    List<User> getAllUsers();
    
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") Long id);
    
    @PostMapping("/users")
    User createUser(@RequestBody User user);
    
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable("id") Long id, @RequestBody User user);
    
    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable("id") Long id);
}