package uk.gov.justice.digital.hmpps.assessments_events.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.Properties

@Component
class LastAccessedEventHelper(
  @Value("\${last-accessed-value.file}") private val fileLocation: String,
  @Qualifier("globalObjectMapper") private val objectMapper: ObjectMapper
) {

  private final val inputStream = FileInputStream(fileLocation)
  private val prop = Properties()

  fun lastAccessedEvent(): LocalDateTime {
    prop.load(inputStream)
    return objectMapper.readValue(prop.getProperty("lastAccessedEvent"), LocalDateTime::class.java)
  }

  fun saveLastAccessedEvent(newLastAccessedEvent: LocalDateTime) {
    prop.load(inputStream)
    prop.setProperty("lastAccessedEvent", objectMapper.writeValueAsString(newLastAccessedEvent))
    val out = FileOutputStream(fileLocation)
    prop.store(out, "")
  }
}
