package pl.dombur.cinema.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.ClassPathResource

@Configuration
class GitCommitConfig {
    @Bean
    fun placeholderConfigurer(): PropertySourcesPlaceholderConfigurer {
        val propsConfig = PropertySourcesPlaceholderConfigurer()
        propsConfig.setLocation(ClassPathResource("git.properties"))
        propsConfig.setIgnoreResourceNotFound(true)
        propsConfig.setIgnoreUnresolvablePlaceholders(true)
        return propsConfig
    }
}
