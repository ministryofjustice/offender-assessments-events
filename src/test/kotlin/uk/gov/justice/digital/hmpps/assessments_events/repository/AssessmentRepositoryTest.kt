package uk.gov.justice.digital.hmpps.assessments_events.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.AssessmentGroup
import uk.gov.justice.digital.hmpps.assessments_events.entity.Offender
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import java.time.LocalDateTime


@SqlGroup(
        Sql(scripts = ["classpath:assessment/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:assessment/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class AssessmentRepositoryTest(@Autowired private val assessmentRepository: AssessmentRepository) : IntegrationTestBase() {

    @Test
    fun returnsAllEventsWithDateCompletedAfter(){

        val eventEntities = assessmentRepository.findByDateCompletedAfterOrderByDateCompleted(LocalDateTime.of(2017, 1,1,1,1))
        assertThat(eventEntities).isEqualTo(listOf(validCompletedAssessment()))
        assertThat(eventEntities.size).isEqualTo(1)
    }

    @Test
    fun returnsCompletedStatusEventsWithDateCompletedAfter(){

        val eventEntities = assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(LocalDateTime.of(2015, 1,1,1,1), "COMPLETE")
        assertThat(eventEntities).isEqualTo(listOf(validCompletedAssessment()))
        assertThat(eventEntities.size).isEqualTo(1)
    }

    fun validCompletedAssessment(): Assessment{
        return Assessment(
            oasysSetPk = 5432,
            assessmentStatus = "COMPLETE",
            assessmentType = "LAYER 3",
            dateCompleted = LocalDateTime.of(2018,6,20,23,0,9),
            group = AssessmentGroup(
                oasysAssessmentGroupPk = 6543,
                offender = Offender(
                    offenderPk = 1234,
                    pnc = "PNC"
                )
            )
        )
    }
//    @Test
//    fun returnsOnlyCompletedAssessmentEvents(){
//
//        val eventEntities = assessmentRepository.findByEventLogPkGreaterThanAndEventTypeCodeEquals(1, "CMP_ASSMT")
//        assertThat(eventEntities).extracting("eventTypeCode").containsOnly("CMP_ASSMT")
//    }
}