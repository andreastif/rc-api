package com.rc.rcapi.configs;

import com.rc.rcapi.models.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Value("${open.ai.api.key}")
    public String openAiKey;

    @Bean
    public ChatModel chatModel() {
        return new ChatModel(openAiKey);
    }


}
