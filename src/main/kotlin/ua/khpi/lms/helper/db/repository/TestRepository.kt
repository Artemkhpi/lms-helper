package ua.khpi.lms.helper.db.repository

import org.springframework.stereotype.Component
import ua.khpi.lms.helper.db.mapper.TestMapper
import ua.khpi.lms.helper.db.repository.jpa.TestJpaRepository
import ua.khpi.lms.helper.model.Test

@Component
class TestRepository(private val repository: TestJpaRepository,
                     private val mapper: TestMapper) {
    fun getAll(): MutableSet<Test> {
        return repository.findAll().mapNotNullTo(mutableSetOf()) { mapper.fromJpa(it) }
    }

    fun findById(id: Long): Test? {
        return mapper.fromJpa(repository.findById(id).orElse(null))
    }

    fun add(test: Test): Test {
        return mapper.fromJpa(repository.save(mapper.toJpa(test)!!))!!
    }

    fun addAll(tests: List<Test>): List<Test> {
        return repository.saveAll(tests.map { mapper.toJpa(it) }).map { mapper.fromJpa(it)!! }
    }

    fun update(test: Test): Test? {
        return mapper.fromJpa(repository.save(mapper.toJpa(test)!!))
    }

    fun remove(id: Long) {
        repository.deleteById(id)
    }
}
