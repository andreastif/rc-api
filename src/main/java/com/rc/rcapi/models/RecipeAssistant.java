package com.rc.rcapi.models;

import com.rc.rcapi.domains.Recipe;
import com.rc.rcapi.domains.StructuredRecipePrompt;


public interface RecipeAssistant {

    Recipe createRecipeFrom(StructuredRecipePrompt ingredients);

}


