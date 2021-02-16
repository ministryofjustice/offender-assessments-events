package uk.gov.justice.digital.hmpps.assessments_events.integration

import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class EventsControllerTest : IntegrationTestBase() {

  @Test
  fun `access forbidden when no authority`() {
    webTestClient.get().uri("/events")
      .header("Content-Type", "application/json")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `trigger events endpoint`() {
    webTestClient.post().uri("/events")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
  }

  @Test
  fun `trigger events endpoint with date`() {
    val date = "2021-02-12T09:46:08.004"
    webTestClient.post().uri("/events/date/$date")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isOk
  }
}
