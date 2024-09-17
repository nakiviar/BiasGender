package com.nak;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nak.model.Author;
import com.nak.model.Book;
import com.nak.service.IAuthorService;
import com.nak.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;


@SpringBootApplication
@RequiredArgsConstructor
public class SpringAiApplication implements ApplicationRunner {

    private final ResourceLoader resourceLoader;
    private final IAuthorService authorService;
    private final IBookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource1 = resourceLoader.getResource("classpath:json/authors.json");
        Resource resource2 = resourceLoader.getResource("classpath:json/books.json");

        // Leer el contenido del archivo JSON
        byte[] jsonData1 = FileCopyUtils.copyToByteArray(resource1.getInputStream());
        byte[] jsonData2 = FileCopyUtils.copyToByteArray(resource2.getInputStream());

        String jsonString1 = new String(jsonData1, StandardCharsets.UTF_8);
        String jsonString2 = new String(jsonData2, StandardCharsets.UTF_8);

        // Puedes usar una librería como Jackson para convertir el JSON a objetos Java
        ObjectMapper objectMapper = new ObjectMapper();
        List<Author> authors = objectMapper.readValue(jsonString1, new TypeReference<>(){});
        List<Book> books = objectMapper.readValue(jsonString2, new TypeReference<>(){});

        authorService.saveAll(authors);
        bookService.saveAll(books);
    }
}
