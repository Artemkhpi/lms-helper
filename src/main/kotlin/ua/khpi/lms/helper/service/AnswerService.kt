package ua.khpi.lms.helper.service

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.repository.AnswerRepository
import ua.khpi.lms.helper.model.Answer

@Service
class AnswerService(private val answerRepository: AnswerRepository) {
    fun getAll(): MutableSet<Answer> {
        return answerRepository.getAll()
    }

    fun findById(id: Long) =
            answerRepository.findById(id)

    fun add(answer: Answer): Answer {
        return answerRepository.add(answer)
    }

    fun update(answer: Answer): Answer {
        return answerRepository.update(answer)!!
    }

    fun remove(id: Long) {
        answerRepository.remove(id)
    }

}
