package uk.gov.justice.digital.hmpps.assessments_events.integration

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @TestConfiguration
  internal class AwsTestConfig {
    @Bean
    @Primary
    @ConditionalOnProperty(name = ["sns.provider"], havingValue = "localstack")
    fun awsSnsClient(): AmazonSNS {
      return AmazonSNSClientBuilder.standard()
        .withCredentials(localStack.defaultCredentialsProvider)
        .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SNS))
        .build()
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = ["sns.provider"], havingValue = "localstack")
    fun awsSqsClient(): AmazonSQSAsync {
      return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(localStack.defaultCredentialsProvider)
        .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SQS))
        .build()
    }

    @Bean
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    fun queueUrl(
      @Autowired awsSqsClient: AmazonSQS,
      @Value("\${sqs.queue.name}") queueName: String
    ): String = awsSqsClient.getQueueUrl(queueName).queueUrl
  }

  companion object {
    @Container
    internal val localStack = LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.2"))
      .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS)
      .withClasspathResourceMapping("/localstack/setup-sns.sh", "/docker-entrypoint-initaws.d/setup-sns.sh", BindMode.READ_WRITE)
      .withEnv("HOSTNAME_EXTERNAL", "localhost")
      .withEnv("DEFAULT_REGION", "eu-west-2")
      .withReuse(true)
  }
}
