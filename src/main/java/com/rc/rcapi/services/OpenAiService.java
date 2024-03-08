package com.rc.rcapi.services;


import com.rc.rcapi.domains.Recipe;
import com.rc.rcapi.domains.StructuredRecipePrompt;
import com.rc.rcapi.models.Model;
import com.rc.rcapi.models.RecipeAssistant;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    //docs: https://docs.langchain4j.dev/category/tutorials
    private RecipeAssistant recipeAssistant;

    @Autowired
    public OpenAiService(Model chatModel) {
        this.recipeAssistant = AiServices.create(RecipeAssistant.class, chatModel.getModel());
    }

    public Recipe send(String ingredients) {
        return recipeAssistant.createRecipeFrom(new StructuredRecipePrompt(ingredients));
    }


}
