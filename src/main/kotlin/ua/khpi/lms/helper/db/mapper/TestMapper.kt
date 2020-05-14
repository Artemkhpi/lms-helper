package ua.khpi.lms.helper.db.mapper

import org.springframework.stereotype.Service
import ua.khpi.lms.helper.db.entity.TestEntity
import ua.khpi.lms.helper.model.Test

@Service
class TestMapper(val questionMapper: QuestionMapper) {
    fun toJpa(test: Test?): TestEntity? {
        if (test == null) return null
        test.let {
            return TestEntity(
                    testId = it.testId,
                    questions = it.questions.mapNotNull { question -> questionMapper.toJpa(question) }.toMutableSet()
            )
        }
    }

    fun fromJpa(testEntity: TestEntity?): Test? {
        if (testEntity == null) return null
        testEntity.let {
            return Test(
                    testId = it.testId,
                    questions = it.questions.mapNotNull { question -> questionMapper.fromJpa(question) }.toMutableList()
            )
        }
    }
}
