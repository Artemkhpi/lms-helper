package ua.khpi.lms.helper.model.kms

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Article(
        @JsonProperty("article_id")
        val articleId: Long? = null,
        @JsonProperty
        val title: String,
        @JsonProperty("keywords")
        val keyWords: List<String>,
        @JsonProperty
        val scope: String,
        @JsonProperty
        val complexity: String,
        @JsonProperty
        val content: List<KmsContentPart>
)