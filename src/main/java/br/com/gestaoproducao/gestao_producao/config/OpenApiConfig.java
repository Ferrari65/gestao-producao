package br.com.gestaoproducao.gestao_producao.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import io.swagger.v3.oas.annotations.servers.Server;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Gestão de Produção — API",
                version = "v1",
                description = "API para produtos, custos, produção e relatórios.",
                contact = @Contact(name = "Yasmin Ferrari", email = "seuemail@exemplo.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Dev local")
        }
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Gestão de Produção — API")
                        .version("v1")
                        .license(new License().name("MIT")));
    }
}