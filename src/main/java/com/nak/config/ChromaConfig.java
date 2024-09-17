package com.nak.config;

import org.springframework.ai.chroma.ChromaApi;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.ChromaVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ChromaConfig {

    //Se agregó esto para indicar el chromaUrl y no de problemas al levantar el proyecto
    @Bean
    public ChromaApi chromaApi(RestClient.Builder restClientBuilder) {
        String chromaUrl = "http://localhost:8000";
        ChromaApi chromaApi = new ChromaApi(chromaUrl, restClientBuilder);
        return chromaApi;
    }

    @Bean
    public ChromaVectorStore chromaVectorStore(OpenAiEmbeddingModel openAiEmbeddingModel, ChromaApi chromaApi) {
        return new ChromaVectorStore(openAiEmbeddingModel, chromaApi, true);
    }
}
