package ua.khpi.lms.helper.model

data class ScormCourse(
        var courseId: Long? = null,
        var title: String,
        var scope: String,
        var complexity: String,
        var keyWords: MutableSet<String> = mutableSetOf(),
        var creator: String,
        var content: String? = null,
        var fileName: String? = null
)