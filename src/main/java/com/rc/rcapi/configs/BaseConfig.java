package com.rc.rcapi.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.rc.rcapi.models.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class BaseConfig {

    @Value("${open.ai.api.key}")
    public String openAiKey;


    @Bean
    public ChatModel chatModel() {
        return new ChatModel(openAiKey);
    }


}
