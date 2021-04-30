package uk.gov.justice.digital.hmpps.assessments_events.integration.controller

import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

@ActiveProfiles("test")
class EventsControllerTest : IntegrationTestBase() {

  @Test
  fun `trigger events endpoint`() {
    webTestClient.get().uri("/events")
      .exchange()
      .expectStatus().isOk
  }

  @Test
  fun `trigger events endpoint with date`() {
    val date = "2021-02-12T09:46:08.004"
    webTestClient.get().uri("/events/date/$date")
      .exchange()
      .expectStatus().isOk
  }
}
