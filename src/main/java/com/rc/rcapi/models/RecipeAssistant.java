package com.rc.rcapi.models;

import com.rc.rcapi.domains.Recipe;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface RecipeAssistant {


    @SystemMessage("You're a helpful recipe creator that will create tasty and affordable recipes. Answers should be in JSON format")
    String assistantDefinition();

    @UserMessage("Create a recipe from {{it}}")
    Recipe userMessage(String message);

}
