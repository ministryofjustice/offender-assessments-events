package uk.gov.justice.digital.hmpps.assessments_events.integration.repository

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
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import java.time.LocalDateTime

@SqlGroup(
  Sql(scripts = ["classpath:assessment/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
  Sql(scripts = ["classpath:assessment/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class AssessmentRepositoryTest(@Autowired private val assessmentRepository: AssessmentRepository) : IntegrationTestBase() {

  @Test
  fun `returns single value after date completed`() {

    val eventEntities =
      assessmentRepository.findByDateCompletedAfterOrderByDateCompleted(LocalDateTime.of(2017, 1, 1, 1, 1))
    assertThat(eventEntities).hasSize(1)
    assertThat(eventEntities).isEqualTo(listOf(assessment2018))
  }

  @Test
  fun `returns multiple values after date completed in correct order`() {

    val eventEntities =
      assessmentRepository.findByDateCompletedAfterOrderByDateCompleted(LocalDateTime.of(2015, 1, 1, 1, 1))
    assertThat(eventEntities).hasSize(2)
    assertThat(eventEntities).element(1).isEqualTo(assessment2016)
    assertThat(eventEntities).element(1).isEqualTo(assessment2018)
  }

  @Test
  fun `returns only completed status events after date completed`() {

    val eventEntities = assessmentRepository.findByDateCompletedAfterAndAssessmentStatus(
      LocalDateTime.of(
        2015,
        1,
        1,
        1,
        1
      ),
      "COMPLETE"
    )

    assertThat(eventEntities.size).isEqualTo(1)
    assertThat(eventEntities).isEqualTo(listOf(assessment2018))
  }

  companion object {
    val assessment2018 = validCompletedAssessment(LocalDateTime.of(2018, 6, 20, 23, 0, 9))
    val assessment2016 = validCompletedAssessment(LocalDateTime.of(2016, 7, 20, 2, 0, 9))

    private fun validCompletedAssessment(dateTime: LocalDateTime): Assessment {
      return Assessment(
        oasysSetPk = 5432,
        assessmentStatus = "COMPLETE",
        assessmentType = "LAYER 3",
        dateCompleted = dateTime,
        group = AssessmentGroup(
          oasysAssessmentGroupPk = 6543,
          offender = Offender(
            offenderPk = 1234,
            pnc = "PNC"
          )
        )
      )
    }
  }
}
