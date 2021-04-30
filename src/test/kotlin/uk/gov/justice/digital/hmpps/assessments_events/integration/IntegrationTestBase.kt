package uk.gov.justice.digital.hmpps.assessments_events.integration

import com.amazonaws.services.sqs.AmazonSQS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.assessments_events.service.SnsService
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEventHelper

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  lateinit var awsSqsClient: AmazonSQS

  @Autowired
  lateinit var snsService: SnsService

  @Autowired
  lateinit var queueUrl: String

  @Autowired
  lateinit var lastAccessedEvent: LastAccessedEventHelper
}
