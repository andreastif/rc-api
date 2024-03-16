package com.rc.rcapi.controllers;

import com.rc.rcapi.domains.PromptDto;
import com.rc.rcapi.domains.Recipe;
import com.rc.rcapi.services.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class SecureChatController {

    private final OpenAiService openAiService;

    @Autowired
    public SecureChatController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }


    @PostMapping("/create")
    Recipe createRecipeGpt4(@RequestBody PromptDto input) {
        //todo: change before publishing API, this is just for testing
        return openAiService.createRecipeGPT_3_5(input);
    }
}
