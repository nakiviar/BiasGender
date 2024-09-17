package com.nak.config;

import com.nak.repo.IBookRepo;
import com.nak.service.impl.BookFunctionServiceImpl;
import com.nak.service.impl.MockWeatherService;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FunctionConfig {

    @Bean
    public FunctionCallback weatherFunctionInfo(){
        return FunctionCallbackWrapper.builder(new MockWeatherService())
                .withName("weatherFunction")
                .withDescription("Get the weather in location")
                .withResponseConverter( (response -> "" + response.temp() + response.unit() ))
                .build();
    }

    @Bean
    public FunctionCallback bookInfoFunction(IBookRepo repo){
        return FunctionCallbackWrapper.builder(new BookFunctionServiceImpl(repo))
                .withName("BookInfo")
                .withDescription("Get book info from book name")
                .withResponseConverter( (response -> "" + response.books()))
                .build();
    }
}
