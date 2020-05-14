package ua.khpi.lms.helper.service

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service
import ua.khpi.lms.helper.api.feign.LmsClient
import ua.khpi.lms.helper.model.ScormCourse
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.model.Test
import java.io.File
import java.util.*


@Service
class LmsService(private val lmsClient: LmsClient,
                 private val scormService: ScormService) {
    fun getCourses(): List<ScormCourse> {
        return lmsClient.courses
    }

    fun createCourse(scormCourse: ScormCourse, articles: List<Article>, tests: List<Test>): ScormCourse {
        scormCourse.keyWords.addAll(articles.flatMap { it.keyWords })
        val fileName = scormService.createArchive(scormCourse, articles, tests)
        scormCourse.fileName = fileName
        scormCourse.content = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(File(fileName)))
        return lmsClient.createCourse(scormCourse)
    }
}