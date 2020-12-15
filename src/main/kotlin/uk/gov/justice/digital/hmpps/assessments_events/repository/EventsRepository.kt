package uk.gov.justice.digital.hmpps.assessments_events.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface EventsRepository : JpaRepository<EventEntity, Long> {
    // Todo needs updating for new table OASYS_SET!!!
    // Bring in the entities from offender_assessments_api project: Assessments, AssessmentGroup, Offender .
    // Bring in the OASYS_SET table from offender_assessments_api project: test/resources/db.migration/V1_4_assessment.sql
    // Set up test data from before-test.sql in test/resources/offender or assessment






    fun findByEventLogPkGreaterThanAndEventTypeCodeEquals(eventLogPk: Long, eventTypeCode: String): List<EventEntity>


    // maybe later need to restrict date to last 2 weeks
}