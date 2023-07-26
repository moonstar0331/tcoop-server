package com.genai.tcoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TcoopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcoopApplication.class, args);
    }

}
