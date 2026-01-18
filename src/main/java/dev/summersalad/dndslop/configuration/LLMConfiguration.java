package dev.summersalad.dndslop.configuration;

import dev.summersalad.dndslop.client.LLMClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class LLMConfiguration {

    private final ResourceLoader resourceLoader;

    @Bean
    public LLMClient ollamaClient(final DataSource dataSource, final OllamaChatModel chatModel) {
        return new LLMClient(
                dataSource,
                chatModel,
                resourceLoader.getResource("classpath:ai/system.md")
        );
    }
}
