package pl.dombur.cinema.config

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ContextConfiguration(
    initializers = [
        PostgresTestContainerConfig.DockerPostgreDataSourceInitializer::class,
    ],
)
open class PostgresTestContainerConfig {
    private var postgreDBContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:16.3-alpine")

    companion object {
        var url = ""
        var username = ""
        var password = ""
    }

    init {
        postgreDBContainer.start()
        url = postgreDBContainer.jdbcUrl
        username = postgreDBContainer.username
        password = postgreDBContainer.password
    }

    open class DockerPostgreDataSourceInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=$url",
                "spring.datasource.username=$username",
                "spring.datasource.password=$password",
            )
        }
    }
}
