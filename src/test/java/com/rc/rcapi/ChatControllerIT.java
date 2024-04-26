package com.rc.rcapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.rcapi.domains.PromptDto;
import com.rc.rcapi.domains.Recipe;
import com.rc.rcapi.services.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerIT {

    @Autowired
    private MockMvc mockMvc;
    //used to convert from json to string with jackson
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OpenAiService openAiService;

    @Value("${api.user}")
    private String apiName;

    @Value("${api.pw}")
    private String apiPw;

    @Test
    void whenNotLoggedIn_thenCreateRecipeGPT3_5_shouldAllowReturnNewRecipe() throws Exception {
        String formatString = String.format("%s:%s", apiName, apiPw);
        String base64String = Base64.getEncoder().encodeToString(formatString.getBytes());

        PromptDto prompt = new PromptDto("cucumber, salt, pepper, vinegar, oil", "2", "english");
        Recipe expectedRecipe = new Recipe(
                "Cucumber Salad",
                "A refreshing and simple salad for 2 servings.",
                List.of( "Slice the cucumber thinly.",
                        "In a bowl, combine cucumber slices, salt, pepper, vinegar, and oil.",
                        "Toss the ingredients well to coat the cucumber slices evenly.", "Let the salad marinate in the fridge for 30 minutes.",
                        "Serve chilled as a side dish or a light snack."),
                List.of("1 cucumber (200g)", "1/4 tsp salt", "1/4 tsp pepper", "1 tbsp vinegar", "1 tbsp oil"),
                List.of("salad", "refreshing", "simple", "affordable", "light" ));

        when(openAiService.createRecipeGPT_3_5(any(PromptDto.class))).thenReturn(expectedRecipe);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/create") // URL AND METHOD
                        .header("RC-R-API", base64String) //CUSTOM HEADERS
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT_TYPE
                        .content(mapper.writeValueAsString(prompt))) //CONTENT
                .andExpect(MockMvcResultMatchers.status().isOk()) // RETURN TYPE
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(expectedRecipe.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedRecipe.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.steps[0]").value(expectedRecipe.getSteps().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.steps[1]").value(expectedRecipe.getSteps().get(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.steps[2]").value(expectedRecipe.getSteps().get(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.steps[3]").value(expectedRecipe.getSteps().get(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.steps[4]").value(expectedRecipe.getSteps().get(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[0]").value(expectedRecipe.getIngredients().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[1]").value(expectedRecipe.getIngredients().get(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[2]").value(expectedRecipe.getIngredients().get(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[3]").value(expectedRecipe.getIngredients().get(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingredients[4]").value(expectedRecipe.getIngredients().get(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(expectedRecipe.getTags().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(expectedRecipe.getTags().get(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags[2]").value(expectedRecipe.getTags().get(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags[3]").value(expectedRecipe.getTags().get(3)))
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isNotEmpty();  // Check if not empty
    }

    @Test
    void whenNotLoggedInAndNoCustomHeaders_thenCreateRecipeGPT3_5_shouldReturnUnauthorized() throws Exception {

        PromptDto prompt = new PromptDto("cucumber, salt, pepper, vinegar, oil", "2", "english");
        Recipe expectedRecipe = new Recipe(
                "Cucumber Salad",
                "A refreshing and simple salad for 2 servings.",
                List.of( "Slice the cucumber thinly.",
                        "In a bowl, combine cucumber slices, salt, pepper, vinegar, and oil.",
                        "Toss the ingredients well to coat the cucumber slices evenly.", "Let the salad marinate in the fridge for 30 minutes.",
                        "Serve chilled as a side dish or a light snack."),
                List.of("1 cucumber (200g)", "1/4 tsp salt", "1/4 tsp pepper", "1 tbsp vinegar", "1 tbsp oil"),
                List.of("salad", "refreshing", "simple", "affordable", "light" ));

        when(openAiService.createRecipeGPT_3_5(any(PromptDto.class))).thenReturn(expectedRecipe);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/create") // URL AND METHOD
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT_TYPE
                        .content(mapper.writeValueAsString(prompt))) //CONTENT
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()); // RETURN TYPE

    }

}
