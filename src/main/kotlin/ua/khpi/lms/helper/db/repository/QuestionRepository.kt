package ua.khpi.lms.helper.db.repository

import org.springframework.stereotype.Component
import ua.khpi.lms.helper.db.mapper.QuestionMapper
import ua.khpi.lms.helper.db.repository.jpa.QuestionJpaRepository
import ua.khpi.lms.helper.model.Question

@Component
class QuestionRepository(private val repository: QuestionJpaRepository,
                         private val mapper: QuestionMapper) {
    fun getAll(): MutableSet<Question> {
        return repository.findAll().mapNotNullTo(mutableSetOf()) { mapper.fromJpa(it) }
    }

    fun findById(id: Long): Question {
        return mapper.fromJpa(repository.findById(id).get())!!
    }

    fun add(question: Question): Question {
        return mapper.fromJpa(repository.save(mapper.toJpa(question)!!))!!
    }

    fun addAll(questions: List<Question>): List<Question> {
        return repository.saveAll(questions.map { mapper.toJpa(it) }).map { mapper.fromJpa(it)!! }
    }

    fun update(question: Question): Question? {
        return mapper.fromJpa(repository.save(mapper.toJpa(question)!!))
    }

    fun remove(id: Long) {
        repository.deleteById(id)
    }
}
