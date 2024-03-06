package com.rc.rcapi.services;


import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OpenAiService {

    //todo: START HERE -> https://docs.langchain4j.dev/category/tutorials


    // longterm memory
    // https://github.com/langchain4j/langchain4j-examples/blob/main/other-examples/src/main/java/ServiceWithPersistentMemoryForEachUserExample.java

    // runtime memory
    // https://github.com/langchain4j/langchain4j-examples/blob/main/other-examples/src/main/java/ServiceWithMemoryExample.java

    // Setting up a one way one response model without chat memory (chat memory is basically feeding in the previous messages to the context window...)
    // https://github.com/andreastif/converter/blob/master/src/main/java/com/example/converter/services/OpenAiService.java

    // "Json mode", structured outputs and more
    // https://github.com/langchain4j/langchain4j-examples/blob/main/other-examples/src/main/java/OtherServiceExamples.java
    public String send(String message) {

        CompletableFuture<String> response = CompletableFuture.supplyAsync( () -> {
            //make api call here and return value?
            return null;
        });

        //do something with the response

        //return the response we did something with

        return null;
    }



}
