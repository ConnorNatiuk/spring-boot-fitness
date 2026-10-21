package com.natiuk.aiservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component 
public class EnvCheck implements CommandLineRunner {
    @Value("${gemini.api.url}")
    private String url;

    @Value("${gemini.api.key}")
    private String key;

    @Override
    public void run(String... args) {
        System.out.println("Gemini URL: " + url);
        System.out.println("Gemini key loaded: " + (key != null && !key.isBlank()));
    }
}
