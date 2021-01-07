package uk.gov.justice.digital.hmpps.assessments_events.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.util.Properties

@Component
class LastAccessedEventHelper(
  @Value("\${last-accessed-value.file}") private val fileLocation: String,
  @Qualifier("globalObjectMapper") private val objectMapper: ObjectMapper
) {

  private final val file = File(javaClass.getResource(fileLocation).file)
  private val prop = Properties()
  private val input = FileInputStream(file)

  fun lastAccessedEvent(): LocalDateTime {
    prop.load(input)
    return objectMapper.readValue(prop.getProperty("lastAccessedEvent"), LocalDateTime::class.java)
  }

  fun saveLastAccessedEvent(newLastAccessedEvent: LocalDateTime) {
    prop.load(input)
    prop.setProperty("lastAccessedEvent", objectMapper.writeValueAsString(newLastAccessedEvent))
    val out: OutputStream = FileOutputStream(file)
    prop.store(out, "")
  }
}
