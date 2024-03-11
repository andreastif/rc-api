package com.rc.rcapi.domains;

import lombok.Builder;

@Builder
public record PromptInput(String ingredientsAndOrRecipe, String language, String servings) {
}
