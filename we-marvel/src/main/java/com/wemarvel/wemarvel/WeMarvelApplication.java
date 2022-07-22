package com.wemarvel.wemarvel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeMarvelApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeMarvelApplication.class, args);
    }

}
