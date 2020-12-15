package uk.gov.justice.digital.hmpps.assessments_events

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import org.mockito.ArgumentCaptor
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

//@SpringBootTest(classes = [OffenderAssessmentsEvents::class])
class SnsServiceTest : IntegrationTestBase() {

    @SpyBean
    lateinit var awsSnsClient: AmazonSNS

    @Autowired
    lateinit var awsSqsClient: AmazonSQS

    @Autowired
    lateinit var snsService: SnsService

    @Autowired
    lateinit var queueUrl: String

    @BeforeEach
    fun setUp() {
        awsSqsClient.purgeQueue(PurgeQueueRequest(queueUrl))
    }

    @Test
    fun testSendEvent() {
        val eventDto = EventDto(
            oasysOffenderPk = 1L,
            assessmentType = "",
            offenderPNC = "",
            eventDate = LocalDateTime.of(2020, 1,1,1,1),
            eventType = EventType.ASSESSMENT_COMPLETED
        )
        val request = ArgumentCaptor.forClass(PublishRequest::class.java)

        snsService.sendEventSNS(eventDto)

        verify(awsSnsClient).publish(request.capture())

        assertThat(request.value.message).isEqualTo("{\"oasysOffenderPk\":1,\"offenderPNC\":\"\",\"assessmentType\":\"\",\"eventDate\":[2020,1,1,1,1],\"eventType\":\"ASSESSMENT_COMPLETED\"}")
        assertThat(request.value.topicArn).isEqualTo("arn:aws:sns:eu-west-2:000000000000:offender_assessments_events")
    }

    @Test
    fun testSendEventIsReceived() {
        val eventDto = EventDto(
            oasysOffenderPk = 1L,
            assessmentType = "",
            offenderPNC = "",
            eventDate = LocalDateTime.of(2020, 1,1,1,9),
            eventType = EventType.ASSESSMENT_COMPLETED
        )
        snsService.sendEventSNS(eventDto)

        val message = getNextMessageOnTestQueue()
        assertThat(message).isEqualTo("")

    }

    private fun getNextMessageOnTestQueue() = awsSqsClient.receiveMessage(queueUrl).messages[0].body

}