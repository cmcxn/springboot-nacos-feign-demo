package com.example.consumer.client;

import com.example.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWireMock(port = 8081)
@ActiveProfiles("test")
public class UserServiceClientTest {

    @Autowired
    private UserServiceClient userServiceClient;

    @Test
    public void testGetAllUsers() {
        // 设置模拟响应
        stubFor(get(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"name\":\"张三\",\"email\":\"zhangsan@example.com\"}," +
                                "{\"id\":2,\"name\":\"李四\",\"email\":\"lisi@example.com\"}]")));

        List<User> users = userServiceClient.getAllUsers();
        
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("张三", users.get(0).getName());
        assertEquals("李四", users.get(1).getName());
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        
        // 设置模拟响应
        stubFor(get(urlEqualTo("/api/users/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"name\":\"张三\",\"email\":\"zhangsan@example.com\"}")));

        User user = userServiceClient.getUserById(userId);
        
        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("张三", user.getName());
        assertEquals("zhangsan@example.com", user.getEmail());
    }

    @Test
    public void testCreateUser() {
        User newUser = new User(null, "王五", "wangwu@example.com");
        
        // 设置模拟响应
        stubFor(post(urlEqualTo("/api/users"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":3,\"name\":\"王五\",\"email\":\"wangwu@example.com\"}")));

        User createdUser = userServiceClient.createUser(newUser);
        
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(3L, createdUser.getId());
        assertEquals(newUser.getName(), createdUser.getName());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 2L;
        User updateUser = new User(userId, "李四修改", "lisi_updated@example.com");
        
        // 设置模拟响应
        stubFor(put(urlEqualTo("/api/users/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":2,\"name\":\"李四修改\",\"email\":\"lisi_updated@example.com\"}")));

        User updatedUser = userServiceClient.updateUser(userId, updateUser);
        
        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        assertEquals("李四修改", updatedUser.getName());
        assertEquals("lisi_updated@example.com", updatedUser.getEmail());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 3L;
        
        // 设置模拟响应
        stubFor(delete(urlEqualTo("/api/users/" + userId))
                .willReturn(aResponse()
                        .withStatus(200)));

        // 不应抛出异常
        assertDoesNotThrow(() -> userServiceClient.deleteUser(userId));
    }
}