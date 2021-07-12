package uk.gov.justice.digital.hmpps.assessments_events.integration.service

import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.fasterxml.jackson.annotation.JsonProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.time.LocalDateTime
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventType as EventTypeEnum

class SnsServiceTest : IntegrationTestBase() {

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

    val awsMessage = getNextMessageOnTestQueue()
    val convertedMessage = objectMapper.readValue(awsMessage.message, EventDto::class.java)
    assertThat(convertedMessage).isEqualTo(eventDto)
  }

  private fun getNextMessageOnTestQueue() =
    objectMapper.readValue(awsSqsClient.receiveMessage(queueUrl).messages[0].body, AWSMessage::class.java)

  data class AWSMessage(
    @JsonProperty("Message")
    val message: String
  )
}
