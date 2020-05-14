package ua.khpi.lms.helper.service

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.api.feign.ConfluenceClient
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.model.kms.KmsArticle
import ua.khpi.lms.helper.model.Test


@Service
class KmsService(private val kmsClient: ConfluenceClient) {
    fun getArticles(): List<KmsArticle> {
        return kmsClient.articles.articles
    }

    fun getArticle(articleId: Long): Article {
        return kmsClient.getArticle(articleId)
    }

    fun getArticleTest(articleId: Long): Test? {
        return try {
            kmsClient.getArticleTest(articleId)
        } catch (ex: Exception) {
            System.err.println(ex)
            null
        }
    }
}