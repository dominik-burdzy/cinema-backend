package pl.dombur.cinema.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(corsRegistry: CorsRegistry) {
        corsRegistry
            .addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowedMethods("GET", "OPTIONS", "PUT", "POST", "DELETE", "HEAD", "PATCH")
            .allowCredentials(true)
            .maxAge(3600)
    }
}
