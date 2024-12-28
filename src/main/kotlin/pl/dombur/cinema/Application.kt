package pl.dombur.cinema

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["pl.dombur.cinema.**"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
