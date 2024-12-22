package pl.dombur.cinema.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import java.util.UUID

@Validated
@Configuration
@ConfigurationProperties(prefix = "service-description")
class ServiceDescriptionProperties {
    @NotBlank
    lateinit var id: String

    @NotBlank
    lateinit var name: String

    lateinit var instanceId: UUID
}
