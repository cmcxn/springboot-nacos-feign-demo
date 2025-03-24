package com.example.provider.controller;

import com.example.api.model.User;
import com.example.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class UserController implements UserService {
    
    private final Map<Long, User> userMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public UserController() {
        // 初始化一些测试数据
        User user1 = new User(idGenerator.getAndIncrement(), "张三", "zhangsan@example.com");
        User user2 = new User(idGenerator.getAndIncrement(), "李四", "lisi@example.com");
        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);
    }
    
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }
    
    @Override
    public User getUserById(Long id) {
        return userMap.get(id);
    }
    
    @Override
    public User createUser(User user) {
        user.setId(idGenerator.getAndIncrement());
        userMap.put(user.getId(), user);
        return user;
    }
    
    @Override
    public User updateUser(Long id, User user) {
        if (!userMap.containsKey(id)) {
            return null;
        }
        user.setId(id);
        userMap.put(id, user);
        return user;
    }
    
    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }
}