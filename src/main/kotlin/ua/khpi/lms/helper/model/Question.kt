package ua.khpi.lms.helper.model

data class Question(
        var questionId: Long? = null,
        var question: String,
        var answers: MutableList<Answer>,
        var testId: Long? = null
)