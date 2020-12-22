package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.*
import java.util.*

@Component
class LastAccessedEvent(@Value("\${last-accessed-value.file}") private final val fileLocation : String){

    private final val file = File(javaClass.getResource(fileLocation).file)
    private val prop = Properties()
    private val input = FileInputStream(file)

    fun lastAccessedEvent(): String {

        prop.load(input)
        return prop.getProperty("lastAccessedEvent")// ?: throw FileNotFoundException())
    }

    fun saveLastAccessedEvent(newLastAccessedEvent: String) {

        prop.load(input)
        prop.setProperty("lastAccessedEvent", newLastAccessedEvent)
        val out: OutputStream = FileOutputStream(file)
        prop.store(out, "")
    }
}