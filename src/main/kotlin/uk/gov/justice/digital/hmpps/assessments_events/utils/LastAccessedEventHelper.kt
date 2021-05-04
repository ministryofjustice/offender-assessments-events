package uk.gov.justice.digital.hmpps.assessments_events.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.Properties

@Component
class LastAccessedEventHelper constructor(
  @Value("\${last-accessed-value.file}") private val fileLocation: String,
  @Qualifier("globalObjectMapper") var objectMapper: ObjectMapper
) {

  private val prop = Properties()

  init {
    checkLastAccessedEventFile()
    prop.load(FileInputStream(fileLocation))
  }

  fun lastAccessedEvent(): LocalDateTime {
    return objectMapper.readValue(prop.getProperty("lastAccessedEvent"), LocalDateTime::class.java)
  }

  fun saveLastAccessedEvent(newLastAccessedEvent: LocalDateTime) {
    prop.setProperty("lastAccessedEvent", objectMapper.writeValueAsString(newLastAccessedEvent))
    val out = FileOutputStream(fileLocation)
    prop.store(out, "")
  }

  private fun checkLastAccessedEventFile() {
    val file = File(fileLocation)
    val newFile = file.createNewFile()
    if (newFile) {
      saveLastAccessedEvent(LocalDateTime.now().minusDays(7))
    }
  }
}
