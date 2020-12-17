package uk.gov.justice.digital.hmpps.assessments_events.integration.health

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InfoTest : IntegrationTestBase() {

  @Test
  fun `Info page is accessible`() {
    webTestClient.get()
        .uri("/info")
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .jsonPath("app.name").isEqualTo("Offender Assesment Events")
  }

  @Test
  fun `Info page reports version`() {
    webTestClient.get().uri("/info")
        .exchange()
        .expectStatus().isOk
        .expectBody().jsonPath("build.version").value<String> {
          assertThat(it).startsWith(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
        }
  }

}