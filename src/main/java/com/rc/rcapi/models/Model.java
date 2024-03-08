package com.rc.rcapi.models;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Model {

    private ChatLanguageModel model;

    public Model(@Value("${open.ai.api.key}") String apiKey) {
        model = OpenAiChatModel
                .builder()
                .apiKey(apiKey)
                .responseFormat("json_object")
                .logRequests(true)
                .logResponses(true)
                .modelName(OpenAiChatModelName.GPT_4_1106_PREVIEW)
                .temperature(0.4)
                .maxTokens(1000)
                .build();
    }

}
