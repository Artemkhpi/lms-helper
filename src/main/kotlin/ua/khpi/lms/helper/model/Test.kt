package ua.khpi.lms.helper.model

data class Test(
        var testId: Long,
        var finished: Boolean = false,
        var questions: MutableList<Question> = mutableListOf()
)
