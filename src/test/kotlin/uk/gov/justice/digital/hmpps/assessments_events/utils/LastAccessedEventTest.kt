package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

class LastAccessedEventTest(@Value("\${last-accessed-value.file}") val fileLocation: String) : IntegrationTestBase() {

    @Test
    fun canReadLastAccessedFromProperties(){
        val lastAccessedEvent = LastAccessedEvent(fileLocation)
        val event = lastAccessedEvent.lastAccessedEvent()
        assertThat(event).isEqualTo("sometesttext")
    }
}