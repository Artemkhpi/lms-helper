package ua.khpi.lms.helper.db.repository

import org.springframework.stereotype.Component
import ua.khpi.lms.helper.db.mapper.AnswerMapper
import ua.khpi.lms.helper.db.repository.jpa.AnswerJpaRepository
import ua.khpi.lms.helper.model.Answer

@Component
class AnswerRepository(private val repository: AnswerJpaRepository,
                       private val mapper: AnswerMapper) {
    fun getAll(): MutableSet<Answer> {
        return repository.findAll().mapNotNullTo(mutableSetOf()) { mapper.fromJpa(it) }
    }

    fun findById(id: Long): Answer {
        return mapper.fromJpa(repository.findById(id).get())
    }

    fun add(answer: Answer): Answer {
        return mapper.fromJpa(repository.save(mapper.toJpa(answer)))
    }

    fun addAll(answers: List<Answer>): List<Answer> {
        return repository.saveAll(answers.map { mapper.toJpa(it) }).map { mapper.fromJpa(it) }
    }

    fun update(answer: Answer): Answer? {
        return mapper.fromJpa(repository.save(mapper.toJpa(answer)))
    }

    fun remove(id: Long) {
        repository.deleteById(id)
    }
}
