# RC API
RC API is one of the microservices which supplies and generates recipes for Recipe Companion based on user the user input such as ingredients, servings and the desired language of the returned recipe. 

## Prerequisites
- Java 17+
- Gradle

## Key Dependencies
- Langchain4j: Provides functionalities for integrating OpenAI models.
- Spring Security OAuth2: Handles OAuth2-based authentication and authorization.
- Lombok: Simplifying the code by automatically generating boilerplate code.
- Spring Dotenv: For managing environment variables.

## Configuration
Ensure you have the appropriate environment variables set up for Firebase and other services. Use the .env file to manage your environment variables locally.

## CI/CD
The repository is automatically deployed with merges to master with Jenkins and the included jenkinsfile.

## Overview
The project has the following POST endpoints exposed:
- `/api/v1/create` -> for non-logged in users
- `/api/v1/auth/create` -> for logged in users

Both endpoints accept a `prompt` that is contains the following properties:
- `private String ingredients`
- `private String servings`
- `private String language`

The `prompt` is used to call the OpenAi endpoint and creates a recipe with either `gpt3.5` model or `gpt4_turbo` based on whether the user is logged in or not.

These values are used to create a `Recipe` object that is then sent back to the frontend application which is displayed accordingly.

## Security
The RC-API uses both JWT tokens and the equivalent of http basic (custom header) as the verification methods. 
The JWT tokens are for verifying registered users (Google Firebase). 
The custom header is used to assure that only the frontend application has the right to do the specified calls to the backend.

