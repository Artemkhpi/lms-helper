package ua.khpi.lms.helper.model

data class Answer(
        var answerId: Long? = null,
        var text: String,
        var correct: Boolean,
        var questionId: Long? = null
)