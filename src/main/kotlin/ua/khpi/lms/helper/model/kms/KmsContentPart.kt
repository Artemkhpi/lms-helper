package ua.khpi.lms.helper.model.kms

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class KmsContentPart(
        @JsonProperty("part_id")
        val partId: Long,
        @JsonProperty("html_body")
        val htmlBody: String
)