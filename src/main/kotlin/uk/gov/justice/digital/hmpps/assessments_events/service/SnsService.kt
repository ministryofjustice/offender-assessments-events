package uk.gov.justice.digital.hmpps.assessments_events.service

import com.amazonaws.services.sns.AmazonSNS
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate
import org.springframework.cloud.aws.messaging.core.TopicMessageChannel
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto

@Service
class SnsService ( awsSnsClient: AmazonSNS, @Value("\${sns.topic.arn}") val topicArn: String ) {

    val notificationMessagingTemplate: NotificationMessagingTemplate = NotificationMessagingTemplate(awsSnsClient)
    val topicMessageChannel = TopicMessageChannel(awsSnsClient, topicArn)

    fun sendEventSNS(eventDto: EventDto){
        notificationMessagingTemplate.convertAndSend(topicMessageChannel, eventDto )
    }
}