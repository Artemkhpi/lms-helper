package ua.khpi.lms.helper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class LmsHelperApplication

fun main(args: Array<String>) {
	runApplication<LmsHelperApplication>(*args)
}
