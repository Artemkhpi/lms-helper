package ua.khpi.lms.helper.db.mapper

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.entity.SequenceEntity
import ua.khpi.lms.helper.model.ArticleSequence

@Service
class SequenceMapper {
    fun toJpa(articleSequence: ArticleSequence): SequenceEntity {
        articleSequence.let {
            return SequenceEntity(
                    sequenceId = it.id,
                    articles = it.articles.map { articleId -> articleId.toString() }
            )
        }
    }

    fun fromJpa(sequence: SequenceEntity): ArticleSequence {
        sequence.let {
            return ArticleSequence(
                    id = it.sequenceId!!,
                    articles = it.articles.map { articleId -> articleId.toLong() }
            )
        }
    }
}
