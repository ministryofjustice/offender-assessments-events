package uk.gov.justice.digital.hmpps.assessments_events.integration.service

import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.microsoft.applicationinsights.core.dependencies.google.gson.GsonBuilder
import com.microsoft.applicationinsights.core.dependencies.google.gson.JsonDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventType
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SqlGroup(
  Sql(
    scripts = ["classpath:assessment/before-test.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
  ),
  Sql(
    scripts = ["classpath:assessment/after-test.sql"],
    config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
)
class EventsServiceTest : IntegrationTestBase() {
  private val gson = GsonBuilder().registerTypeAdapter(
    LocalDateTime::class.java,
    JsonDeserializer { json, _, _ ->
      LocalDateTime.parse(
        json.asJsonPrimitive.asString,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
      )
    }
  ).create()

  var lastAccessedDateTime: LocalDateTime = LocalDateTime.of(2016, 6, 20, 23, 0, 9)

  @BeforeEach
  fun before() {
    awsSqsClient.purgeQueue(PurgeQueueRequest(queueUrl))
    lastAccessedEvent.saveLastAccessedEvent(lastAccessedDateTime)
  }

  @AfterEach
  fun after() {
    lastAccessedEvent.saveLastAccessedEvent(lastAccessedDateTime)
  }

  @Test
  fun `Should send and receive one completed assessment`() {

    eventsService.sendNewEventsToTopic()

    await.atMost(Duration.ofSeconds(20)) untilCallTo {
      awsSqsClient.receiveMessage(queueUrl).messages
        .map { message -> gson.fromJson(message.body, AWSMessage::class.java) }
        .map { awsMessage -> gson.fromJson(awsMessage.Message, EventDto::class.java) }
        .toList()
    } matches {
      it?.containsAll(listOf(eventDto)) ?: false
    }

    assertThat(lastAccessedEvent.lastAccessedEvent()).isEqualTo(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
  }

  data class AWSMessage(val Message: String)

  val eventDto = EventDto(
    oasysOffenderPk = 1234L,
    offenderPNC = "PNC",
    assessmentType = "LAYER_3",
    assessmentStatus = "COMPLETE",
    eventDate = LocalDateTime.of(2018, 6, 20, 23, 0, 9),
    eventType = EventType.ASSESSMENT_COMPLETED
  )
}
