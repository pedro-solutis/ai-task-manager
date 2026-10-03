package br.com.solutis.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Task Manager API")
                        .version("v1.0")
                        .description("API RESTful para gerenciamento inteligente de tarefas, utilizando o Spring AI para oferecer sugestões, análise de complexidade e divisão em sub-tarefas.")
                        .contact(new Contact()
                                .name("Pedro Ferreira")
                                .email("pedro.ferreira@solutis.com.br")));
    }
}
