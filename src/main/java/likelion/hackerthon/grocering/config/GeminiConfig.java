package likelion.hackerthon.grocering.config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GeminiConfig {

    @Value("${google.ai.project-id}")
    private String projectId;

    @Value("${google.ai.location}")
    private String location;

    @Value("${google.ai.model-name}")
    private String modelName;

    @Bean
    public GenerativeModel generativeModel() throws IOException {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            return new GenerativeModel(modelName, vertexAI);
        }
    }
}