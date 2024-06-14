package com.example.backoffice.common.utils

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime

@Converter(autoApply = true)
class ZonedDateTimeConverter : AttributeConverter<ZonedDateTime, Timestamp> {
    override fun convertToDatabaseColumn(attribute: ZonedDateTime?): Timestamp? {
        return attribute?.let { Timestamp.from(it.toInstant()) }
    }

    override fun convertToEntityAttribute(dbData: Timestamp?): ZonedDateTime? {
        return dbData?.toInstant()?.atZone(ZoneId.systemDefault())
    }
}