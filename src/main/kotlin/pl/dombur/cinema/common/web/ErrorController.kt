package pl.dombur.cinema.common.web

import jakarta.persistence.EntityNotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pl.dombur.cinema.application.exception.ShowAlreadyDefinedException

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class ErrorController {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound(e: EntityNotFoundException): ResponseEntity<Any> {
        logger.warn(e) { "Cannot find entity" }
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ShowAlreadyDefinedException::class)
    fun handleNotFound(e: ShowAlreadyDefinedException): ResponseEntity<Any> {
        logger.warn(e) { "Conflict on entity" }
        return ResponseEntity(e.message, HttpStatus.CONFLICT)
    }
}
