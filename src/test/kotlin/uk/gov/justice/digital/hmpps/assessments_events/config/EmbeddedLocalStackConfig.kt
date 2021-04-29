package uk.gov.justice.digital.hmpps.assessments_events.config

import javassist.bytecode.stackmap.TypeData.ClassName
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

@Configuration
@ConditionalOnProperty(name = ["sns.provider"], havingValue = "localstack")
class EmbeddedLocalStackConfig {
  private val log = LoggerFactory.getLogger(ClassName::class.java)

  @Bean
  fun localStackContainer(): LocalStackContainer {
    log.info("Starting localstack...")
    val logConsumer = Slf4jLogConsumer(log).withPrefix("localstack")
    val localStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack").withTag("0.11.2"))
      .withServices(LocalStackContainer.Service.SQS, LocalStackContainer.Service.SNS)
      .withClasspathResourceMapping(
        "/localstack/setup-sns.sh",
        "/docker-entrypoint-initaws.d/setup-sns.sh",
        BindMode.READ_WRITE
      )
      .withEnv("HOSTNAME_EXTERNAL", "localhost")
      .withEnv("DEFAULT_REGION", "eu-west-2")
      .waitingFor(
        Wait.forLogMessage(".*SNS Configured.*", 1)
      )
    log.info("Started localstack")
    localStackContainer.start()
    localStackContainer.followOutput(logConsumer)
    return localStackContainer
  }
}
