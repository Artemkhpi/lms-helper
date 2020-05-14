package ua.khpi.lms.helper.model.kms

import com.fasterxml.jackson.annotation.JsonProperty

data class KmsArticle(
        @JsonProperty("article_id")
        var articleId: Long,
        @JsonProperty
        var title: String,
        @JsonProperty("keywords")
        var keyWords: List<String>
)