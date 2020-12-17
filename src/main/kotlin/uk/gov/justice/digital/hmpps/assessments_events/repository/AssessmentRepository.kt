package uk.gov.justice.digital.hmpps.assessments_events.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import java.time.LocalDateTime


@Repository
interface AssessmentRepository : JpaRepository<Assessment, Long> {
    // DONE - needs updating for new table OASYS_SET!!!
    // DONE - Bring in the entities from offender_assessments_api project: Assessments, AssessmentGroup, Offender .
    // TODO - Bring in the OASYS_SET table from offender_assessments_api project: test/resources/db.migration/V1_4_assessment.sql
    // TODO - Set up test data from before-test.sql in test/resources/offender or assessment

    fun findFirst20ByDateCompletedAfterOrderByDateCompleted(dateCompleted: LocalDateTime): List<Assessment>
}