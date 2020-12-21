package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.*
import java.util.*

@Component
class LastAccessedEvent{

    @Value("\${last-accessed-value.file}")
    lateinit var fileLocation : String

    fun lastAccessedEvent(): String {

        val file = File(javaClass.getResource(fileLocation).file)
        val prop = Properties()

        FileInputStream(file).use { prop.load(it) }
        return prop.getProperty("lastAccessedEvent") ?: throw FileNotFoundException()
    }

    fun saveLastAccessedEvent(newLastAccessedEvent: String) {

        val file = File(javaClass.getResource(fileLocation).file)
        val prop = Properties()

        FileInputStream(file).use {
            prop.load(it)
            prop.setProperty("lastAccessedEvent", newLastAccessedEvent)

            val out: OutputStream = FileOutputStream(file)
            prop.store(out, "some comment")
            out.close()
        }
    }
}