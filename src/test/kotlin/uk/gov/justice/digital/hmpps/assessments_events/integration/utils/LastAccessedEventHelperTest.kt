package uk.gov.justice.digital.hmpps.assessments_events.integration.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

class LastAccessedEventHelperTest : IntegrationTestBase() {

  @Test
  fun `Can read Last Accessed Event date from properties`() {

    val event = lastAccessedEvent.lastAccessedEvent()
    assertThat(event).isNotNull
  }
}
