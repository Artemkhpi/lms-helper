package ua.khpi.lms.helper.service

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.repository.QuestionRepository
import ua.khpi.lms.helper.model.Question

@Service
class QuestionService(private val questionRepository: QuestionRepository) {
    fun getAll(): MutableSet<Question> {
        return questionRepository.getAll()
    }

    fun findById(id: Long) =
            questionRepository.findById(id)

    fun add(question: Question): Question {
        return questionRepository.add(question)
    }

    fun update(question: Question): Question {
        return questionRepository.update(question)!!
    }

    fun remove(id: Long) {
        questionRepository.remove(id)
    }

}
