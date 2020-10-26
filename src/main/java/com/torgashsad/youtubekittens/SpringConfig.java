package com.torgashsad.youtubekittens;

import com.google.api.services.youtube.YouTube;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
@ComponentScan("com.torgashsad.youtubekittens")
public class SpringConfig {
    @Bean
    public YouTube youtube() throws GeneralSecurityException, IOException {
        return YTServiceSupplier.getService();
    }
}
