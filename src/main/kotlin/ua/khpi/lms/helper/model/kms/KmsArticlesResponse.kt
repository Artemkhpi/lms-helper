package ua.khpi.lms.helper.model.kms

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class KmsArticlesResponse (
        @JsonProperty
        val articles: List<KmsArticle>,
        @JsonProperty("total_pages")
        val totalPages: Int,
        @JsonProperty
        val currentPage: Int
)