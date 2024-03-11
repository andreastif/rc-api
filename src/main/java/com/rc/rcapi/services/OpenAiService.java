package com.rc.rcapi.services;


import com.rc.rcapi.domains.PromptDto;
import com.rc.rcapi.domains.PromptInput;
import com.rc.rcapi.domains.Recipe;
import com.rc.rcapi.domains.StructuredRecipePrompt;
import com.rc.rcapi.models.ChatModel;
import com.rc.rcapi.models.RecipeAssistant;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    //docs: https://docs.langchain4j.dev/category/tutorials
    private RecipeAssistant recipeAssistant;

    @Autowired
    public OpenAiService(ChatModel chatModel) {
        this.recipeAssistant = AiServices.create(RecipeAssistant.class, chatModel.getModel());
    }

    public Recipe send(PromptDto dto) {

        PromptInput promptInput = PromptInput.builder()
                .ingredientsAndOrRecipe(dto.getIngredients())
                .servings(dto.getServings())
                .language(dto.getLanguage())
                .build();

        return recipeAssistant.createRecipeFrom(new StructuredRecipePrompt(promptInput.ingredientsAndOrRecipe(), promptInput.servings(), promptInput.language()));
    }


}
