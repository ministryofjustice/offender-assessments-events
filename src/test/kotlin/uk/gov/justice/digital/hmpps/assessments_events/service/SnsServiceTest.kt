package uk.gov.justice.digital.hmpps.assessments_events.service

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.microsoft.applicationinsights.core.dependencies.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import java.time.LocalDateTime
import org.mockito.ArgumentCaptor
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventType

import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

class SnsServiceTest : IntegrationTestBase() {

    @SpyBean
    lateinit var awsSnsClient: AmazonSNS

    @Autowired
    lateinit var awsSqsClient: AmazonSQS

    @Autowired
    lateinit var snsService: SnsService

    @Autowired
    lateinit var queueUrl: String

    private val gson = GsonBuilder().create()

    @BeforeEach
    fun setUp() {
        awsSqsClient.purgeQueue(PurgeQueueRequest(queueUrl))
    }

    @Test
    fun testSendEvent() {
        val eventDto = EventDto(
            oasysOffenderPk = 1L,
            assessmentType = "",
            assessmentStatus = "",
            offenderPNC = "",
            eventDate = LocalDateTime.of(2020, 1,1,1,1),
            eventType = EventType.ASSESSMENT_COMPLETED
        )
        val request = ArgumentCaptor.forClass(PublishRequest::class.java)

        snsService.sendEventSNS(eventDto)

        verify(awsSnsClient).publish(request.capture())

        val message = gson.fromJson(request.value.message, EventDto::class.java)
        assertThat(message).isEqualTo(eventDto)
        assertThat(request.value.topicArn).isEqualTo("arn:aws:sns:eu-west-2:000000000000:offender_assessments_events")
    }

    @Test
    fun testSendEventIsReceived() {
        val eventDto = EventDto(
            oasysOffenderPk = 1L,
            assessmentType = "",
            assessmentStatus = "",
            offenderPNC = "",
            eventDate = LocalDateTime.of(2020, 1,1,1,9),
            eventType = EventType.ASSESSMENT_COMPLETED
        )
        snsService.sendEventSNS(eventDto)

        val message = getNextMessageOnTestQueue()
        val convertedMessage = gson.fromJson(message.Message, EventDto::class.java)
        assertThat(convertedMessage).isEqualTo(eventDto)

    }
    private fun getNextMessageOnTestQueue() = gson.fromJson(awsSqsClient.receiveMessage(queueUrl).messages[0].body, Message::class.java)

    data class Source(val Value: String)
    data class MessageAttributes(val eventType: EventDto, val source: Source)
    data class Message(val Message: String, val MessageAttributes: MessageAttributes)

}