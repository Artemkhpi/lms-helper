package ua.khpi.lms.helper.api.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ua.khpi.lms.helper.model.ScormCourse


@FeignClient(name = "lmsClient", url = "http://localhost:8081/v1", configuration = [BasicFeignConfiguration::class])
interface LmsClient {

    @get:RequestMapping(method = [RequestMethod.GET], value = ["/courses"])
    val courses: List<ScormCourse>

    @RequestMapping(method = [RequestMethod.POST], value = ["/course"], consumes = ["application/json"])
    fun createCourse(scormCourse: ScormCourse): ScormCourse
}