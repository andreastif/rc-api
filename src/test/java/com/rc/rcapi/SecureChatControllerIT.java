package com.rc.rcapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rc.rcapi.domains.PromptDto;
import com.rc.rcapi.domains.Recipe;
import com.rc.rcapi.services.OpenAiService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class SecureChatControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OpenAiService openAiService;

    @Value("${api.user}")
    private String apiName;

    @Value("${api.pw}")
    private String apiPw;

    @Test
    @WithMockUser
    void whenLoggedIn_thenCreateRecipeGPT4_shouldAllowReturnNewRecipe() throws Exception {

        log.info("test");

        String formatString = String.format("%s:%s", apiName, apiPw);
        String base64String = Base64.getEncoder().encodeToString(formatString.getBytes());

        PromptDto prompt = new PromptDto("cucumber, salt, pepper, vinegar, oil", "2", "english");
        Recipe expectedRecipe = new Recipe(
                "Simple Cucumber Salad",
                "A refreshing and easy-to-make cucumber salad perfect as a side dish for 2 servings. " +
                        "The crispness of the cucumber is enhanced by the tanginess of the vinegar and the simple seasoning of salt and pepper. " +
                        "This salad is ideal for a light lunch or as an accompaniment to a main meal. " +
                        "It is also a healthy, low-calorie option for those watching their diet.",
                List.of("Thinly slice 200 grams of cucumber.",
                        "In a bowl, whisk together 15 milliliters of vinegar",
                        "30 milliliters of oil", "1 gram of salt, and 1 gram of pepper.",
                        "Add the cucumber slices to the dressing and toss to coat evenly.",
                        "Let the salad sit for 10 minutes to marinate before serving.",
                        "Serve chilled or at room temperature."),
                List.of("200 grams cucumber", "15 milliliters vinegar", "30 milliliters oil", "1 gram salt", "1 gram pepper"),
                List.of("salad", "easy", "healthy", "refreshing", "quick"));

        when(openAiService.createRecipeGPT_4(any(PromptDto.class))).thenReturn(expectedRecipe);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/create") // URL AND METHOD
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
    @WithMockUser
    void whenLoggedInAndNoCustomHeaders_thenCreateRecipeGPT4_shouldReturnUnauthorized() throws Exception {

        PromptDto prompt = new PromptDto("cucumber, salt, pepper, vinegar, oil", "2", "english");
        Recipe expectedRecipe = new Recipe(
                "Simple Cucumber Salad",
                "A refreshing and easy-to-make cucumber salad perfect as a side dish for 2 servings. " +
                        "The crispness of the cucumber is enhanced by the tanginess of the vinegar and the simple seasoning of salt and pepper. " +
                        "This salad is ideal for a light lunch or as an accompaniment to a main meal. " +
                        "It is also a healthy, low-calorie option for those watching their diet.",
                List.of("Thinly slice 200 grams of cucumber.",
                        "In a bowl, whisk together 15 milliliters of vinegar",
                        "30 milliliters of oil", "1 gram of salt, and 1 gram of pepper.",
                        "Add the cucumber slices to the dressing and toss to coat evenly.",
                        "Let the salad sit for 10 minutes to marinate before serving.",
                        "Serve chilled or at room temperature."),
                List.of("200 grams cucumber", "15 milliliters vinegar", "30 milliliters oil", "1 gram salt", "1 gram pepper"),
                List.of("salad", "easy", "healthy", "refreshing", "quick"));

        when(openAiService.createRecipeGPT_4(any(PromptDto.class))).thenReturn(expectedRecipe);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/create") // URL AND METHOD
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT_TYPE
                        .content(mapper.writeValueAsString(prompt))) //CONTENT
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()); // RETURN TYPE
    }

    @Test
    void whenNotLoggedIn_thenCreateRecipeGPT4_shouldReturnUnauthorized() throws Exception {


        PromptDto prompt = new PromptDto("cucumber, salt, pepper, vinegar, oil", "2", "english");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/create") // URL AND METHOD
                        .contentType(MediaType.APPLICATION_JSON) // CONTENT_TYPE
                        .content(mapper.writeValueAsString(prompt))) //CONTENT
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()); // RETURN TYPE
    }
}