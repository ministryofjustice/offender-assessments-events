package uk.gov.justice.digital.hmpps.assessments_events.service

import com.amazonaws.services.sns.AmazonSNS
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate
import org.springframework.cloud.aws.messaging.core.TopicMessageChannel
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto

@Service
class SnsService(awsSnsClient: AmazonSNS, @Value("\${sns.topic.arn}") val topicArn: String, @Qualifier("globalObjectMapper") var objectMapper: ObjectMapper) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  val notificationMessagingTemplate: NotificationMessagingTemplate = NotificationMessagingTemplate(awsSnsClient)
  val topicMessageChannel = TopicMessageChannel(awsSnsClient, topicArn)

  fun sendEventSNS(eventDtos: Collection<EventDto>) {
    log.info("Sending ${eventDtos.size} events to SNS topic")

    for (eventDto in eventDtos) {
      notificationMessagingTemplate.convertAndSend(
        topicMessageChannel,
        objectMapper.writeValueAsString(eventDto),
        mapOf("eventType" to eventDto.eventType.value)
      )
      log.info("Sent event to SNS topic")
    }
  }
}
