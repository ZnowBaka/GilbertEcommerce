package com.example.gilbertecommerce;

import com.example.gilbertecommerce.Framework.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GilbertEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GilbertEcommerceApplication.class, args);
        SecurityConfig securityConfig = new SecurityConfig();
        System.out.println(securityConfig.passwordEncoder().encode("abc123"));
    }



}
