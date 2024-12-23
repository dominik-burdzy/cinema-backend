package pl.dombur.cinema

import org.springframework.boot.actuate.autoconfigure.audit.AuditAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableRetry
@EnableScheduling
@SpringBootApplication(
    scanBasePackages = ["pl.dombur.cinema.**"],
    exclude = [
        AuditAutoConfiguration::class,
        SessionAutoConfiguration::class,
        RedisAutoConfiguration::class,
        RedisReactiveAutoConfiguration::class,
        RedisRepositoriesAutoConfiguration::class,
    ],
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
