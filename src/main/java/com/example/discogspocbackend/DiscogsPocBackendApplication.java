package com.example.discogspocbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        DiscogsPocContext.class
})
@SpringBootConfiguration
@EnableAutoConfiguration
public class DiscogsPocBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscogsPocBackendApplication.class, args);
    }

}
