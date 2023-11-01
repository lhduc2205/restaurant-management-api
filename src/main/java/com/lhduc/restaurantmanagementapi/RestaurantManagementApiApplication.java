package com.lhduc.restaurantmanagementapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootApplication
public class RestaurantManagementApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementApiApplication.class, args);
    }

}
