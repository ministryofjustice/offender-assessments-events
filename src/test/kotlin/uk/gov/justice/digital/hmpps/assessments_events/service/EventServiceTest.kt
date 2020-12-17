package uk.gov.justice.digital.hmpps.assessments_events.service

import io.mockk.mockk
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository

class EventServiceTest: IntegrationTestBase() {
    val assessmentRepository: AssessmentRepository = mockk()


}