package uk.gov.justice.digital.hmpps.assessments_events.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import java.time.LocalDateTime

@Repository
interface AssessmentRepository : JpaRepository<Assessment, Long> {

  fun findByDateCompletedAfterOrderByDateCompleted(dateCompleted: LocalDateTime): List<Assessment>

  fun findByDateCompletedAfterAndAssessmentStatusIn(
    dateCompleted: LocalDateTime,
    assessmentStatuses: Set<String>
  ): Collection<Assessment>
}
