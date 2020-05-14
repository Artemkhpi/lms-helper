package ua.khpi.lms.helper.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.view.RedirectView
import ua.khpi.lms.helper.model.Answer
import ua.khpi.lms.helper.model.Question
import ua.khpi.lms.helper.model.Test
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.model.kms.KmsContentPart
import ua.khpi.lms.helper.service.*


@Controller
class NavigationController(private val articleService: ArticleService,
                           private val lmsService: LmsService,
                           private val kmsService: KmsService,
                           private val testService: TestService,
                           private val questionService: QuestionService,
                           private val answerService: AnswerService) {
    @GetMapping("/")
    fun getArticlesPage(model: Model): String {
        model.addAttribute("articles", articleService.getAll())
        return "availableContent"
    }

    @GetMapping("/courses")
    fun getImportedCoursesPage(model: Model): String {
        model.addAttribute("courses", lmsService.getCourses())
        return "importedCourses"
    }

    @GetMapping("/article/{articleId}")
    fun getArticlePage(model: Model,
                       @PathVariable articleId: Long): String {
        model.addAttribute("article", kmsService.getArticle(articleId))
        model.addAttribute("tests", articleService.getArticleTest(articleId))
        //model.addAttribute("article", getMockedArticle(articleId))
        //model.addAttribute("tests", getMockedTests(articleId))
        return "article"
    }

    @GetMapping("/course/{courseId}/tests")
    fun getTestsPage(model: Model,
                     @PathVariable courseId: Long): String {
        val articleId: Long = 1
        model.addAttribute("courseId", courseId)
        val tests = mutableListOf<Test>()
        tests.addAll(articleService.getArticleSequence(courseId).articles.mapNotNull { kmsService.getArticleTest(articleId) })

        tests.add(getMockedTest(articleId))

        val manualTest = testService.findById(articleId)
        if (manualTest != null) {
            tests.add(manualTest)
        }
        model.addAttribute("tests", tests)
        return "tests"
    }

    @GetMapping("/course/{courseId}/tests/add")
    fun getNewTestPage(model: Model,
                       @PathVariable courseId: Long): String {
        model.addAttribute("courseId", courseId)
        model.addAttribute("test", testService.findById(courseId))
        //model.addAttribute("test", getMockedTest(courseId))
        return "addTest"
    }

    @GetMapping("/course/{courseId}/tests/submit")
    fun submitNewTest(model: Model,
                       @PathVariable courseId: Long): RedirectView {
        val test = testService.findById(courseId)!!
        test.finished = true
        testService.update(test)
        return RedirectView("/course/$courseId/general-info")
    }

    @GetMapping("/course/{courseId}/tests/question/add")
    fun getNewQuestionPage(model: Model,
                           @PathVariable courseId: Long): String {
        model.addAttribute("courseId", courseId)
        return "addQuestion"
    }

    @PostMapping("/course/{courseId}/tests/question/add")
    fun createNewQuestion(model: Model,
                          @PathVariable courseId: Long,
                          @RequestBody formData: MultiValueMap<String, String>): RedirectView {
        val singleValueMap = formData.toSingleValueMap()
        var test = testService.findById(courseId)
        if (test == null) {
            test = Test(testId = courseId)
            test = testService.add(test)
        }
        val question = questionService.add(Question(question = singleValueMap["question"]!!, testId = test.testId, answers = mutableListOf()))
        answerService.add(Answer(text = singleValueMap["answer1"]!!, correct = true, questionId = question.questionId))
        answerService.add(Answer(text = singleValueMap["answer2"]!!, correct = false, questionId = question.questionId))
        answerService.add(Answer(text = singleValueMap["answer3"]!!, correct = false, questionId = question.questionId))
        answerService.add(Answer(text = singleValueMap["answer4"]!!, correct = false, questionId = question.questionId))
        return RedirectView("/course/$courseId/tests/add")
    }

    private fun getMockedArticle(articleId: Long): Article {
        return when (articleId) {
            1L -> Article(articleId = articleId,
                    title = "some title for first article",
                    keyWords = listOf("java", "development", "linux"),
                    scope = "Java developer",
                    complexity = "Junior",
                    content = listOf(
                            KmsContentPart(1, htmlBody = "<div>some content</div>"),
                            KmsContentPart(2, htmlBody = "<div>different content</div>")
                    ))
            3L -> Article(articleId = articleId,
                    title = "third article",
                    keyWords = listOf("linux", "os"),
                    scope = "dev ops",
                    complexity = "Junior",
                    content = listOf(
                            KmsContentPart(1, htmlBody = "<div>content content content</div>")
                    ))
            5L -> Article(articleId = articleId,
                    title = "article with id 5",
                    keyWords = listOf("kotlin", "development", "jvm"),
                    scope = "backend developer",
                    complexity = "middle",
                    content = listOf(
                            KmsContentPart(1, htmlBody = "<div>kotlin kotlin kotlin</div>"),
                            KmsContentPart(2, htmlBody = "<div>jvm jvm jvm jvm</div>")
                    ))
            else -> Article(articleId = articleId,
                    title = "db monster",
                    keyWords = listOf("db", "sql", "mysql", "oraclesql", "query"),
                    scope = "db developer",
                    complexity = "middle",
                    content = listOf(
                            KmsContentPart(1, htmlBody = "<div>some db text exercise</div>")
                    ))
        }
    }

    private fun getMockedTest(articleId: Long): Test {
        return Test(
                testId = articleId,
                questions = mutableListOf(
                        Question(questionId = articleId,
                                question = "choose correct answer",
                                answers = mutableListOf(
                                        Answer(answerId = articleId,
                                                text = "incorrect",
                                                correct = false),
                                        Answer(answerId = articleId + 1,
                                                text = "correct",
                                                correct = true)
                                )),
                        Question(questionId = articleId + 1,
                                question = "created",
                                answers = mutableListOf(
                                        Answer(answerId = articleId,
                                                text = "false",
                                                correct = true),
                                        Answer(answerId = articleId + 1,
                                                text = "true",
                                                correct = false)
                                )
                        )))
    }
}