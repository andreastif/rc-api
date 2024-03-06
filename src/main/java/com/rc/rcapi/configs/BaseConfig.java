package com.rc.rcapi.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Value("${open.ai.api.key}")
    public String openAiKey;


}
