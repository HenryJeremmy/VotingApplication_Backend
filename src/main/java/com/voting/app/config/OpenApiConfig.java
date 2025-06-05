package com.voting.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
  info = @Info(
    title = "Voting Management API",
    version = "1.0.0",
    description = "A code‑first example with Springdoc and Swagger UI",
    contact = @Contact(name = "Emmanuel Kemakolam", email = "emmakema47@example.com"),
    license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
  ),
  servers = @Server(url = "http://localhost:8080")
)
public class OpenApiConfig {
}