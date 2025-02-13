package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.demo
 *
 * @author : idasom
 * @data : 2/12/25
 */
@SpringBootApplication(exclude = SecurityException.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
