package pl.dombur.cinema.common.web

import jakarta.persistence.EntityNotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class ErrorController {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound(e: EntityNotFoundException): ResponseEntity<Any> {
        logger.warn(e) { "Cannot find entity" }
        return ResponseEntity(e.message, HttpStatusCode.valueOf(404))
    }
}
