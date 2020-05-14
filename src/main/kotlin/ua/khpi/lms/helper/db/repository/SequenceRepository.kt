package ua.khpi.lms.helper.db.repository

import org.springframework.stereotype.Component
import ua.khpi.lms.helper.db.mapper.SequenceMapper
import ua.khpi.lms.helper.db.repository.jpa.SequenceJpaRepository
import ua.khpi.lms.helper.model.ArticleSequence

@Component
class SequenceRepository(private val repository: SequenceJpaRepository,
                         private val mapper: SequenceMapper) {
    fun findById(id: Long): ArticleSequence {
        return mapper.fromJpa(repository.findById(id).get())
    }

    fun add(sequence: ArticleSequence): ArticleSequence {
        return mapper.fromJpa(repository.save(mapper.toJpa(sequence)))
    }

    fun update(sequence: ArticleSequence): ArticleSequence? {
        return mapper.fromJpa(repository.save(mapper.toJpa(sequence)))
    }

    fun remove(id: Long) {
        repository.deleteById(id)
    }
}
