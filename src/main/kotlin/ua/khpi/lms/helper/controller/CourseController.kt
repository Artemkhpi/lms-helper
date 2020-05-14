package ua.khpi.lms.helper.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import ua.khpi.lms.helper.model.Test
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.service.ArticleService
import ua.khpi.lms.helper.service.CourseService
import ua.khpi.lms.helper.service.LmsService
import ua.khpi.lms.helper.service.TestService

@Controller
class CourseController(private val articleService: ArticleService,
                       private val courseService: CourseService,
                       private val testService: TestService,
                       private val lmsService: LmsService) {
    @PostMapping(value = ["course/articles"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun populateArticlesPage(
            @RequestParam(name = "articles") ids: ArrayList<Long>): ResponseEntity<String> {
        return ResponseEntity.ok("course/${articleService.createArticleSequence(ids)}/positions")
    }

    @PostMapping(value = ["course/{courseId}/positions"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun populatePositionsPage(model: Model,
                              @PathVariable courseId: Long,
                              @RequestBody formData: MultiValueMap<String, String>): RedirectView {
        articleService.updateArticleSequence(courseId, formData.toSingleValueMap())
        return RedirectView("/course/$courseId/tests")
    }

    @GetMapping(value = ["course/{courseId}/positions"])
    fun getArticlesPositionsPage(model: Model,
                                 @PathVariable courseId: Long): String {
        model.addAttribute("articles", articleService.getArticleSequence(courseId).articles.let { articleService.findAll(it) })
        return "positions"
    }

    @GetMapping(value = ["/course/{courseId}/general-info"])
    fun getFinishCourseCreationPage(model: Model,
                                    @PathVariable courseId: Long): String {
        model.addAttribute("courseId", courseId)
        model.addAttribute("complexity",
                articleService.getArticleSequence(courseId).articles.let {
                    articleService.getArticlesWithFullInformation(it).map { article ->
                        article.complexity
                    }.toSet()
                })
        return "courseCreation"
    }

    @PostMapping(value = ["course/create"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun createCourse(@RequestBody formData: MultiValueMap<String, String>): RedirectView {
        val singleValueMap = formData.toSingleValueMap()
        var articles: List<Article>
        singleValueMap["courseId"]!!.toLong().let {
            articleService.getArticleSequence(it).articles.let { id ->
                articles = articleService.getArticlesWithFullInformation(id)
            }
        }
        val tests = testService.findById(singleValueMap["courseId"]!!.toLong())
        val course = lmsService.createCourse(
                courseService.createCourse(
                        title = singleValueMap["name"]!!,
                        scope = singleValueMap["scope"]!!,
                        complexity = singleValueMap["complexity"]!!),
                articles,
                listOf(tests!!)
        )
        println(course)
        return RedirectView("/courses")
    }
}