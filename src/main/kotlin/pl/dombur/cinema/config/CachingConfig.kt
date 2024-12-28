package pl.dombur.cinema.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class CachingConfig {
    companion object {
        const val OMDB_MOVIE_CACHE = "omdbMovieCache"
    }

    @Bean
    fun cacheManager(): ConcurrentMapCacheManager =
        ConcurrentMapCacheManager(
            OMDB_MOVIE_CACHE,
        )
}
