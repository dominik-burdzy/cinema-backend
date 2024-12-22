package pl.dombur.cinema.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

private val logger = KotlinLogging.logger {}

@Configuration
class ApplicationEventLoggingConfig(
    private val properties: ServiceDescriptionProperties,
) {
    @Value("\${git.commit.id}")
    private lateinit var gitCommitId: String

    @EventListener
    fun onStartup(event: ApplicationReadyEvent) {
        logger.info {
            "Started service '${properties.name}' " +
                "with instanceId: '${properties.instanceId}' " +
                "and gitCommitId: '$gitCommitId'"
        }
    }
}
