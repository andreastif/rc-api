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

    /**
     * <a href="https://firebase.google.com/docs/admin/setup#java_1">...</a>
     * setDatabaseUrl is optional
     */
    @Bean
    FirebaseApp createFireBaseApp() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("recept-aabt-firebase-adminsdk.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();
        return FirebaseApp.initializeApp(options);
    }


}
