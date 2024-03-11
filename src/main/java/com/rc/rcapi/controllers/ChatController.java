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
@RequestMapping("/api/v1/")
public class ChatController {

    private final OpenAiService openAiService;

    @Autowired
    public ChatController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/")
    Recipe SendMessage(@RequestBody PromptDto input) {
        return openAiService.send(input);
    }

}
