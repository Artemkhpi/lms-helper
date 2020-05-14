package ua.khpi.lms.helper.service

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.model.ScormCourse

@Service
class CourseService {
    fun createCourse(title: String, complexity: String, scope: String): ScormCourse {
        return ScormCourse(title = title, scope = scope, complexity = complexity, creator = "LMS helper")
    }
}
