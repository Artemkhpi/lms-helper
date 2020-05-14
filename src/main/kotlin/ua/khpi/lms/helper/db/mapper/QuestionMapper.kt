package ua.khpi.lms.helper.db.mapper

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.entity.QuestionEntity
import ua.khpi.lms.helper.model.Question
import javax.annotation.PostConstruct

@Service
class QuestionMapper(val answerMapper: AnswerMapper) {
    fun toJpa(question: Question?): QuestionEntity? {
        if (question == null) return null
        question.let {
            return QuestionEntity(
                    questionId = it.questionId,
                    question = it.question,
                    answers = it.answers.map { answer -> answerMapper.toJpa(answer) }.toMutableSet(),
                    testId = it.testId
            )
        }
    }

    fun fromJpa(questionEntity: QuestionEntity?): Question? {
        if (questionEntity == null) return null
        questionEntity.let {
            return Question(
                    questionId = it.questionId,
                    question = it.question,
                    answers = it.answers.map { answer -> answerMapper.fromJpa(answer) }.toMutableList(),
                    testId = it.testId
            )
        }
    }
}
