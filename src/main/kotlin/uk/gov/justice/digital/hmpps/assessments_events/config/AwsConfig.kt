package uk.gov.justice.digital.hmpps.assessments_events.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AwsConfig {
  @Bean
  @ConditionalOnProperty(name = ["sns.provider"], havingValue = "aws")
  @Primary
  fun awsSnsClient(
    @Value("\${sns.aws.access.key.id}") accessKey: String,
    @Value("\${sns.aws.secret.access.key}") secretKey: String,
    @Value("\${cloud.aws.region.static}") region: String
  ): AmazonSNS =
    BasicAWSCredentials(accessKey, secretKey)
      .let {
        AmazonSNSClientBuilder.standard()
          .withCredentials(AWSStaticCredentialsProvider(it))
          .withRegion(region)
          .build()
      }
}
