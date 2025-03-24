package com.example.consumer.client;

import com.example.api.model.User;
import com.example.api.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "provider-service", path = "/api")
public interface UserServiceClient extends UserService {
    // 继承UserService接口中的所有方法，无需重复定义
}