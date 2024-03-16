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
    private RecipeAssistant recipeAssistant_GPT3_5;
    private RecipeAssistant recipeAssistant_GPT4;

    @Autowired
    public OpenAiService(ChatModel chatModel) {
        this.recipeAssistant_GPT3_5 = AiServices.create(RecipeAssistant.class, chatModel.getGPT_3_5());
        this.recipeAssistant_GPT4 = AiServices.create(RecipeAssistant.class, chatModel.getGPT_4_1106());
    }

    public Recipe createRecipeGPT_4(PromptDto dto) {

        PromptInput promptInput = PromptInput.builder()
                .ingredientsAndOrRecipe(dto.getIngredients())
                .servings(dto.getServings())
                .language(dto.getLanguage())
                .build();

        return recipeAssistant_GPT4.createRecipeFrom(new StructuredRecipePrompt(promptInput.ingredientsAndOrRecipe(), promptInput.servings(), promptInput.language()));
    }

    public Recipe createRecipeGPT_3_5(PromptDto dto) {

        PromptInput promptInput = PromptInput.builder()
                .ingredientsAndOrRecipe(dto.getIngredients())
                .servings(dto.getServings())
                .language(dto.getLanguage())
                .build();

        return recipeAssistant_GPT3_5.createRecipeFrom(new StructuredRecipePrompt(promptInput.ingredientsAndOrRecipe(), promptInput.servings(), promptInput.language()));
    }


}
