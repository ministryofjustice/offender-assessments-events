package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

class LastAccessedEventTest : IntegrationTestBase() {

    @Autowired
    lateinit var lastAccessedEvent: LastAccessedEvent

    @Test
    fun canReadLastAccessedFromProperties(){

        val event = lastAccessedEvent.lastAccessedEvent()
        assertThat(event).isEqualTo("sometesttext")
    }

    @Test
    fun canUpdateLastAccessedFromProperties(){

        val event = lastAccessedEvent.saveLastAccessedEvent("somenewtext")
        //assertThat(event).isEqualTo("sometesttext")
    }
}