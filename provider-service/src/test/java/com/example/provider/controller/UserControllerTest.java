package com.example.provider.controller;

import com.example.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                getBaseUrl() + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {}
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<User> users = response.getBody();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    public void testGetUserById() {
        // 先获取所有用户
        List<User> users = restTemplate.exchange(
                getBaseUrl() + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {}
        ).getBody();
        
        assertNotNull(users);
        assertTrue(users.size() > 0);
        
        // 测试获取第一个用户
        Long userId = users.get(0).getId();
        ResponseEntity<User> response = restTemplate.getForEntity(
                getBaseUrl() + "/users/{id}", 
                User.class, 
                userId
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    public void testCreateUser() {
        User newUser = new User(null, "王五", "wangwu@example.com");
        
        ResponseEntity<User> response = restTemplate.postForEntity(
                getBaseUrl() + "/users",
                newUser,
                User.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User createdUser = response.getBody();
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(newUser.getName(), createdUser.getName());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        // 先创建一个用户
        User newUser = new User(null, "赵六", "zhaoliu@example.com");
        User createdUser = restTemplate.postForObject(
                getBaseUrl() + "/users",
                newUser,
                User.class
        );
        
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        
        // 更新用户
        createdUser.setName("赵六修改");
        createdUser.setEmail("zhaoliu_updated@example.com");
        
        ResponseEntity<User> response = restTemplate.exchange(
                getBaseUrl() + "/users/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(createdUser),
                User.class,
                createdUser.getId()
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User updatedUser = response.getBody();
        assertNotNull(updatedUser);
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals("赵六修改", updatedUser.getName());
        assertEquals("zhaoliu_updated@example.com", updatedUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        // 先创建一个用户
        User newUser = new User(null, "测试删除", "test_delete@example.com");
        User createdUser = restTemplate.postForObject(
                getBaseUrl() + "/users",
                newUser,
                User.class
        );
        
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        
        // 删除用户
        restTemplate.delete(getBaseUrl() + "/users/{id}", createdUser.getId());
        
        // 验证用户是否已删除
        ResponseEntity<User> response = restTemplate.getForEntity(
                getBaseUrl() + "/users/{id}",
                User.class,
                createdUser.getId()
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}