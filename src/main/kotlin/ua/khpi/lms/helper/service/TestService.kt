package ua.khpi.lms.helper.service

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.repository.TestRepository
import ua.khpi.lms.helper.model.Test

@Service
class TestService(private val testRepository: TestRepository) {
    fun getAll(): MutableSet<Test> {
        return testRepository.getAll()
    }

    fun findById(id: Long) =
            testRepository.findById(id)

    fun add(test: Test): Test {
        return testRepository.add(test)
    }

    fun update(test: Test): Test {
        return testRepository.update(test)!!
    }

    fun remove(id: Long) {
        testRepository.remove(id)
    }

}
