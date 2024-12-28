package pl.dombur.cinema.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
@ConfigurationProperties(prefix = "client.omdb")
class OmdbClientProperties {
    @NotBlank
    lateinit var url: String

    @NotBlank
    lateinit var apiKey: String
}
