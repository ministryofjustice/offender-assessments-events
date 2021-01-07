package uk.gov.justice.digital.hmpps.assessments_events.config

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.testcontainers.containers.localstack.LocalStackContainer

@Configuration
class SnsConfig(val localStackContainer: LocalStackContainer) {

  @Bean
  @Primary
  fun awsSnsClient(): AmazonSNS =
    AmazonSNSClientBuilder.standard()
      .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SNS))
      .build()

  @Bean
  fun awsSqsClient(): AmazonSQS =
    AmazonSQSClientBuilder.standard()
      .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
      .build()

  @Bean
  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  fun queueUrl(
    @Autowired awsSqsClient: AmazonSQS,
    @Value("\${sqs.queue.name}") queueName: String
  ): String = awsSqsClient.getQueueUrl(queueName).queueUrl
}
