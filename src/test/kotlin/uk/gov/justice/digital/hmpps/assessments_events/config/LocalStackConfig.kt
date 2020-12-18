package uk.gov.justice.digital.hmpps.assessments_events.config

//import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
//import com.amazonaws.services.sns.AmazonSNS
//import com.amazonaws.services.sns.AmazonSNSClientBuilder
//import com.amazonaws.services.sqs.AmazonSQS
//import com.amazonaws.services.sqs.AmazonSQSClientBuilder
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//
//@Configuration
//class LocalStackConfig {
//
//    @Bean
//    @ConditionalOnProperty(name = ["sns.provider"], havingValue = "localstack", matchIfMissing = true)
//    @Primary
//    fun awsSnsClient(
//        @Value("\${sns.endpoint.url}") serviceEndpoint: String,
//        @Value("\${cloud.aws.region.static}") region:String
//    ): AmazonSNS =
//        AmazonSNSClientBuilder.standard()
//            .withRegion(region)
//            .withEndpointConfiguration(EndpointConfiguration(serviceEndpoint, region))
//            .build()
//
//    @Bean
//    fun awsSqsClient(
//        @Value("\${sqs.endpoint.url}") serviceEndpoint: String,
//        @Value("\${cloud.aws.region.static}") region: String
//    ): AmazonSQS =
//        AmazonSQSClientBuilder.standard()
//            .withRegion(region)
//            .withEndpointConfiguration(EndpointConfiguration(serviceEndpoint, region))
//            .build()

//    @Bean
//    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
//    fun queueUrl(
//        @Autowired awsSqsClient: AmazonSQS,
//        @Value("\${sqs.queue.name}") queueName: String
//    ): String = awsSqsClient.getQueueUrl(queueName).queueUrl
//}
