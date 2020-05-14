package ua.khpi.lms.helper.api.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.model.kms.KmsArticlesResponse
import ua.khpi.lms.helper.model.Test


@FeignClient(name = "kmsClient", url = "http://192.168.0.116:1990/confluence/rest/learningobject/1.0", configuration = [BasicFeignConfiguration::class, KmsFeignConfiguration::class])
interface ConfluenceClient {
    @get:RequestMapping(method = [RequestMethod.GET], value = ["/article"])
    val articles: KmsArticlesResponse

    @RequestMapping(method = [RequestMethod.GET], value = ["/article/{articleId}"])
    fun getArticle(@PathVariable("articleId") articleId: Long): Article

    @RequestMapping(method = [RequestMethod.GET], value = ["/article/{articleId}/tests"])
    fun getArticleTest(@PathVariable("articleId") articleId: Long): Test
}