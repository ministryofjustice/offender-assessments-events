package uk.gov.justice.digital.hmpps.assessments_events.utils


import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.io.OutputStream
import java.util.*

class LastAccessedEventTest : IntegrationTestBase() {

    val lastAccessedEvent= LastAccessedEvent()

    @Test
    fun canReadLastAccessedFromProperties(){

        val event = lastAccessedEvent.lastAccessedEvent()
        assertThat(event).isEqualTo("sometesttext")
    }

    @Test
    fun canUpdateLastAccessedFromProperties(){
        val key = slot<String>()
        val value = slot<String>()
        val prop: Properties = mockk()

        lastAccessedEvent.saveLastAccessedEvent("somenewtext")

        verify {prop.setProperty(capture(key), capture(value)) }
        verify(exactly = 1) { prop.store(any<OutputStream>(), "") }
        assertThat(key).isEqualTo("")
        assertThat(key).isEqualTo("somenewtext")

    }
}