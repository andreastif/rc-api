package com.rc.rcapi.domains;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@StructuredPrompt("You're a helpful recipe assistant that will create tasty and affordable recipes from a select few ingredients. " +
        "Utilize the given ingredients as much as possible using different cooking techniques to be as affordable as possible." +
        "If the recipe can be improved, give a maximum of two extra ingredients that can be bought for optimal affordability" +
        "Create a recipe from {{ingredients}} and give all quantities in the metric system.")
public class StructuredRecipePrompt {
    private String ingredients;
}
