package com.nak.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.ChromaVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/embeddings")
@RequiredArgsConstructor
public class EmbeddingController {

    private final OpenAiEmbeddingModel openAiEmbeddingModel;
    private final ChromaVectorStore vectorStore;

    @GetMapping("/generate")
    public Map<String, EmbeddingResponse> generateEmbeddings(@RequestParam("message") String message) {
        EmbeddingResponse response = openAiEmbeddingModel.embedForResponse(List.of(message));

        return Map.of("embedding", response);
    }

    @GetMapping("/vectorstore")
    public List<Document> useVectorStore(@RequestParam("message") String message) {

        List<Document> documents = List.of(
                new Document("Spring AI es lo maximo", Map.of("meta1", "meta1")),
                new Document("Python es mas popular en IA"),
                new Document("El futuro es la Inteligencia Artificial", Map.of("meta1", "meta1"))
        );

        vectorStore.add(documents);

        List<Document> results = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(1));

        return results;
    }

    @GetMapping("/searchCandidates")
    public List<Document> searchCandidates(@RequestParam("jobDescription") String jobDescription) {

        // Curriculums o perfiles de candidatos almacenados como documentos
        List<Document> candidates = List.of(
                new Document("Desarrolladora Java con 5 años de experiencia en Spring y Azure",
                        Map.of("skills", "Java, Spring, Azure", "gender", "female", "yearsExperience", "5")),
                new Document("Desarrollador Java con 4 años de experiencia en Spring y Azure",
                        Map.of("skills", "Java, Spring, Azure", "gender", "male", "yearsExperience", "4")),
                new Document("Ingeniero de datos con experiencia en Python, Hadoop y análisis de grandes volúmenes de datos",
                        Map.of("skills", "Python, Hadoop, Big Data", "gender", "male", "yearsExperience", "7")),
                new Document("Especialista en inteligencia artificial con conocimiento en Python y TensorFlow",
                        Map.of("skills", "Python, TensorFlow, AI", "gender", "female", "yearsExperience", "3"))
        );

        // Almacenar los perfiles en la vector store
        vectorStore.add(candidates);

        // Realizar una búsqueda de similitud utilizando la descripción del puesto
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.query(jobDescription)
                        .withTopK(1) // Trae el mejor resultado
        );

        // Opcional: Filtrar o anonimizar información para evitar sesgos antes de devolver los resultados
        List<Document> anonymizedResults = results.stream()
                .map(doc -> new Document(doc.getContent(),
                        doc.getMetadata().entrySet().stream()
                                .filter(entry -> !entry.getKey().equals("gender")) // Filtrar el género para evitar sesgo
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                ))
                .collect(Collectors.toList());

        return anonymizedResults; // Devolver los resultados anonimizados
    }
}

