package uk.gov.justice.digital.hmpps.assessments_events.integration.service

import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.fasterxml.jackson.annotation.JsonProperty
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
  fun `Should send and receive two guillotined assessments and one completed assessment`() {

    eventsService.sendNewEventsToTopic()

    var allMessages = listOf<Message>()
    await.atMost(Duration.ofSeconds(5)) untilCallTo {
      while (allMessages.size < 3) {
        allMessages = listOf(allMessages, awsSqsClient.receiveMessage(queueUrl).messages).flatten()
      }
      allMessages
        .map { message -> objectMapper.readValue(message.body, AWSMessage::class.java) }
        .map { awsMessage -> objectMapper.readValue(awsMessage.message, EventDto::class.java) }
        .toList()
    } matches {
      println(it)
      it?.containsAll(listOf(assessmentGuillotined2, assessmentGuillotined, assessmentCompleted)) ?: false
    }
    assertThat(lastAccessedEvent.lastAccessedEvent()).isEqualTo(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
  }

  data class AWSMessage(
    @JsonProperty("Message")
    val message: String
  )

  val assessmentCompleted = EventDto(
    oasysOffenderPk = 1234L,
    offenderPNC = "PNC",
    assessmentType = "LAYER_3",
    assessmentStatus = "COMPLETE",
    eventDate = LocalDateTime.of(2018, 6, 20, 23, 0, 9),
    eventType = EventType.ASSESSMENT_COMPLETED
  )
  val assessmentGuillotined = EventDto(
    oasysOffenderPk = 1234L,
    offenderPNC = "PNC",
    assessmentType = "LAYER_3",
    assessmentStatus = "GUILLOTINED",
    eventDate = LocalDateTime.of(2016, 7, 20, 10, 0, 9),
    eventType = EventType.ASSESSMENT_GUILLOTINED
  )
  val assessmentGuillotined2 = EventDto(
    oasysOffenderPk = 1234L,
    offenderPNC = "PNC",
    assessmentType = "LAYER_3",
    assessmentStatus = "GUILLOTINED",
    eventDate = LocalDateTime.of(2016, 7, 20, 2, 0, 9),
    eventType = EventType.ASSESSMENT_GUILLOTINED
  )
}
