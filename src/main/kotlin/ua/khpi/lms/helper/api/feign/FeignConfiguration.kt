package ua.khpi.lms.helper.api.feign

import feign.okhttp.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import feign.auth.BasicAuthRequestInterceptor



@Configuration
class BasicFeignConfiguration {

    @Bean
    fun client(): OkHttpClient {
        return OkHttpClient()
    }

}

@Configuration
class KmsFeignConfiguration {

    @Bean
    fun basicAuthRequestInterceptor(): BasicAuthRequestInterceptor {
        return BasicAuthRequestInterceptor("admin", "admin")
    }
}