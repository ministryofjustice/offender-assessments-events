package uk.gov.justice.digital.hmpps.assessments_events.integration

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
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
    fun awsSnsClient(): AmazonSNS {
      return AmazonSNSClientBuilder.standard()
        .withCredentials(localStack.defaultCredentialsProvider)
        .withEndpointConfiguration(localStack.getEndpointConfiguration(LocalStackContainer.Service.SNS))
        .build()
    }

    @Bean
    @Primary
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
    internal val localStack = LocalStackContainer(DockerImageName.parse("localstack/localstack:0.10.5"))
      .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS)
      .withEnv("DEFAULT_REGION", "eu-west-2")
      .withReuse(true)

    @BeforeAll
    @JvmStatic
    fun beforeAll() {
      localStack.execInContainer("awslocal", "sns", "create-topic", "--name", "offender_assessments_events")
      localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "test_queue")
      localStack.execInContainer(
        "awslocal",
        "--endpoint-url=http://localhost:4575",
        "sns",
        "subscribe",
        "--topic-arn",
        "arn:aws:sns:eu-west-2:000000000000:offender_assessments_events",
        "--protocol",
        "sqs",
        "--notification-endpoint",
        "http://localhost:4576/queue/test_queue"
      )
    }
  }
}
