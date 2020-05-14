package ua.khpi.lms.helper.service

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.repository.SequenceRepository
import ua.khpi.lms.helper.model.ArticleSequence
import ua.khpi.lms.helper.model.kms.Article
import ua.khpi.lms.helper.model.kms.KmsArticle
import ua.khpi.lms.helper.model.kms.KmsContentPart
import ua.khpi.lms.helper.model.Test

@Service
class ArticleService(private val sequenceRepository: SequenceRepository,
                     private val kmsService: KmsService) {

    fun getAll(): List<KmsArticle> {
        return kmsService.getArticles()
        /*return listOf(
                KmsArticle(1, "some title for first article", listOf("java", "development", "linux")),
                KmsArticle(3, "third article", listOf("linux", "os")),
                KmsArticle(5, "article with id 5", listOf("kotlin", "development", "jvm")),
                KmsArticle(15, "db monster", listOf("db", "sql", "mysql", "oraclesql", "query"))
        )*/
    }

    fun findAll(ids: List<Long>): List<KmsArticle> {
        return getAll().filter { it.articleId in ids }
    }

    fun getArticlesWithFullInformation(ids: List<Long>): List<Article> {
        //return getFullArticles().filter { it.articleId in ids }
        return ids.map { kmsService.getArticle(it) }.toList()
    }

    fun getArticleTest(id: Long): Test? {
        return kmsService.getArticleTest(id)
    }

    fun getFullArticles(): List<Article> {
        return listOf(Article(articleId = 1,
                title = "some title for first article",
                keyWords = listOf("java", "development", "linux"),
                scope = "Java developer",
                complexity = "Junior",
                content = listOf(
                        KmsContentPart(1, htmlBody = "<div>some content</div>"),
                        KmsContentPart(2, htmlBody = "<div>different content</div>")
                )), Article(articleId = 3,
                title = "third article",
                keyWords = listOf("linux", "os"),
                scope = "dev ops",
                complexity = "Junior",
                content = listOf(
                        KmsContentPart(1, htmlBody = "<div>content content content</div>")
                )), Article(articleId = 5,
                title = "article with id 5",
                keyWords = listOf("kotlin", "development", "jvm"),
                scope = "backend developer",
                complexity = "middle",
                content = listOf(
                        KmsContentPart(1, htmlBody = "<div>kotlin kotlin kotlin</div>"),
                        KmsContentPart(2, htmlBody = "<div>jvm jvm jvm jvm</div>")
                )), Article(articleId = 15,
                title = "db monster",
                keyWords = listOf("db", "sql", "mysql", "oraclesql", "query"),
                scope = "db developer",
                complexity = "middle",
                content = listOf(
                        KmsContentPart(1, htmlBody = "<div>some db text exercise</div>")
                )))
    }

    fun createArticleSequence(ids: List<Long>): Long {
        val sequence = sequenceRepository.add(ArticleSequence(articles = ids))
        return sequence.id!!
    }

    fun updateArticleSequence(sequenceId: Long, idMap: Map<String, String>) {
        val sequence = sequenceRepository.findById(sequenceId)
        sequence.articles = idMap.toList().sortedBy { (_, value) -> value }.toMap().map { it.key.toLong() }.toList()
        sequenceRepository.update(sequence)
    }

    fun getArticleSequence(id: Long) = sequenceRepository.findById(id)
}
