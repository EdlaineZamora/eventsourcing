package com.br.events.store.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.br.events.store.*"})
public class HotelProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelProviderApplication.class, args);
    }

}
