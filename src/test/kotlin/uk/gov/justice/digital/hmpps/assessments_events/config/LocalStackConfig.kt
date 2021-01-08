package uk.gov.justice.digital.hmpps.assessments_events.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

@Configuration
class LocalStackConfig {

  @Bean
  fun localStackContainer(): LocalStackContainer {
    println("LocalStack container starting")

    val localStackContainer: LocalStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack").withTag("0.10.5"))
      .withServices(LocalStackContainer.Service.SQS, LocalStackContainer.Service.SNS)
      .withClasspathResourceMapping("/localstack/setup-sns.sh", "/docker-entrypoint-initaws.d/setup-sns.sh", BindMode.READ_WRITE)
      .withEnv("HOSTNAME_EXTERNAL", "localhost")
      .withEnv("DEFAULT_REGION", "eu-west-2")
      .waitingFor(
        Wait.forLogMessage(".*SNS Configured.*", 1)
      )

    localStackContainer.start()
    println("LocalStack container started")
    return localStackContainer
  }
}
