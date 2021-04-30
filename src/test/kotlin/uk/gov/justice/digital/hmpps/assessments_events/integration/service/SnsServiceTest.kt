package uk.gov.justice.digital.hmpps.assessments_events.integration.service

import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.microsoft.applicationinsights.core.dependencies.google.gson.GsonBuilder
import com.microsoft.applicationinsights.core.dependencies.google.gson.JsonDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventType as EventTypeEnum

class SnsServiceTest : IntegrationTestBase() {

  private val gson = GsonBuilder().registerTypeAdapter(
    LocalDateTime::class.java,
    JsonDeserializer { json, _, _ -> LocalDateTime.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) }
  )
    .create()

  @BeforeEach
  fun setUp() {
    awsSqsClient.purgeQueue(PurgeQueueRequest(queueUrl))
  }

  @Test
  fun testSendEventIsReceived() {
    val eventDto = EventDto(
      oasysOffenderPk = 1L,
      assessmentType = "",
      assessmentStatus = "",
      offenderPNC = "",
      eventDate = LocalDateTime.of(2020, 1, 1, 1, 9),
      eventType = EventTypeEnum.ASSESSMENT_COMPLETED
    )
    snsService.sendEventSNS(listOf(eventDto))

    val message = getNextMessageOnTestQueue()
    val convertedMessage = gson.fromJson(message.Message, EventDto::class.java)
    assertThat(convertedMessage).isEqualTo(eventDto)
  }

  private fun getNextMessageOnTestQueue() =
    gson.fromJson(awsSqsClient.receiveMessage(queueUrl).messages[0].body, Message::class.java)

  data class EventType(val Value: String)
  data class Source(val Value: String)
  data class MessageAttributes(val eventType: EventType, val source: Source)
  data class Message(val Message: String, val MessageAttributes: MessageAttributes)
}
