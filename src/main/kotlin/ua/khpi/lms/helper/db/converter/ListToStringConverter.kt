package ua.khpi.lms.helper.mocks.db.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class ListToStringConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(stringList: List<String>): String {
        return stringList.joinToString(separator = SPLIT_CHAR)
    }

    override fun convertToEntityAttribute(value: String): List<String> {
        return if (value.isNotBlank()) {
            value.split(SPLIT_CHAR).toCollection(mutableListOf())
        } else {
            mutableListOf()
        }
    }

    companion object {
        private const val SPLIT_CHAR = ";"
    }
}