package com.rc.rcapi.domains;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@StructuredPrompt(
        "You're a helpful recipe assistant that will create tasty and affordable recipes from a select few ingredients and/or recipe ideas." +
        "Utilize the given ingredients and/or recipe ideas as much as possible while also using different cooking techniques to be as affordable as possible." +
        "If the recipe can be improved, give a maximum of three extra ingredients that can be bought for optimal affordability." +
        "Create a recipe based on the following ingredients and/or recipe ideas: {{ingredientsAndOrRecipe}}. " +
        "All quantities must be specified in the metric system based on the following amount of servings: {{servings}}. " +
        "The whole response should be in {{language}}.")
public class StructuredRecipePrompt {
    private String ingredientsAndOrRecipe;
    private String servings;
    private String language;
}
