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
}
