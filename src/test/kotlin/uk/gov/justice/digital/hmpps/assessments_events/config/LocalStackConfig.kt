package uk.gov.justice.digital.hmpps.assessments_events.config

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.testcontainers.containers.localstack.LocalStackContainer

@Configuration
@ConditionalOnProperty(name = ["sns.provider"], havingValue = "localstack")
class LocalStackConfig(@Autowired private val localStackContainer: LocalStackContainer) {

  @Bean
  @Primary
  fun awsSnsClient(): AmazonSNS {
    return AmazonSNSClientBuilder.standard()
      .withCredentials(localStackContainer.defaultCredentialsProvider)
      .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SNS))
      .build()
  }

  @Bean
  @Primary
  fun awsSqsClient(): AmazonSQSAsync {
    return AmazonSQSAsyncClientBuilder.standard()
      .withCredentials(localStackContainer.defaultCredentialsProvider)
      .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
      .build()
  }

  @Bean
  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  fun queueUrl(
    @Autowired awsSqsClient: AmazonSQS,
    @Value("\${sqs.queue.name}") queueName: String
  ): String = awsSqsClient.getQueueUrl(queueName).queueUrl
}
