package uk.gov.justice.digital.hmpps.assessments_events.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.time.LocalDateTime

class LastAccessedEventHelperTest : IntegrationTestBase() {

  @Autowired
  lateinit var lastAccessedEvent: LastAccessedEventHelper

  @Test
  fun canReadLastAccessedFromProperties() {

    val event = lastAccessedEvent.lastAccessedEvent()
    assertThat(event).isEqualTo(LocalDateTime.of(2020, 1, 1, 1, 1))
  }
}
