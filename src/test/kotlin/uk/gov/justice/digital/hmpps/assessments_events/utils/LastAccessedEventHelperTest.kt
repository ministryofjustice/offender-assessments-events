package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class LastAccessedEventHelperTest {

  @Autowired
  lateinit var lastAccessedEvent: LastAccessedEventHelper

  @Test
  fun `Can read Last Accessed Event date from properties`() {

    val event = lastAccessedEvent.lastAccessedEvent()
    assertThat(event).isNotNull
  }
}
