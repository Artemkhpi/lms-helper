package ua.khpi.lms.helper.db.mapper

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.entity.AnswerEntity
import ua.khpi.lms.helper.model.Answer

@Service
class AnswerMapper{
    fun toJpa(answer: Answer): AnswerEntity {
        answer.let {
            return AnswerEntity(
                    answerId = it.answerId,
                    text = it.text,
                    correct = it.correct,
                    questionId = it.questionId
            )
        }
    }

    fun fromJpa(answerEntity: AnswerEntity): Answer {
        answerEntity.let {
            return Answer(
                    answerId = it.answerId,
                    text = it.text,
                    correct = it.correct,
                    questionId = it.questionId
            )
        }
    }
}
