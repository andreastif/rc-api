package com.rc.rcapi.domains;


import dev.langchain4j.model.output.structured.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Description("short title, 8 words maximum")
    private String title;

    @Description("short description, 4 sentences maximum")
    private String description;

    @Description("each step should be described in 1 sentence maximum")
    private List<String> steps;

    @Description("a list of all ingredients used and their quantities")
    private List<String> ingredients;

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", steps=" + steps +
                '}';
    }
}
