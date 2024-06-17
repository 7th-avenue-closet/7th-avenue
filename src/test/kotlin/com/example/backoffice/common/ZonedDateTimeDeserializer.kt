package com.example.backoffice.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime>() {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): ZonedDateTime {
        return ZonedDateTime.parse(jp.text, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Asia/Seoul")))
    }
}