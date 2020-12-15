package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

@Component
class LastAccessedEvent (@Value("\${last-accessed-value.file}") var fileLocation: String) {

     fun lastAccessedEvent(): String{

         val file = File(javaClass.getResource(fileLocation).file)
         val prop = Properties()

        FileInputStream(file).use { prop.load(it) }
        return prop.getProperty("lastAccessedEvent") ?: throw FileNotFoundException()
    }

    fun saveLastAccessedEvent(newLastAccessedEvent:String){

        val file = File(javaClass.getResource(fileLocation).file)
        val prop = Properties()
        prop.load(FileInputStream(file))
        prop.setProperty("lastAccessedEvent", newLastAccessedEvent)
        prop.store(FileOutputStream(file), null)
    }
}