package uk.gov.justice.digital.hmpps.assessments_events.dto

import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.AssessmentStatusType
import java.time.LocalDateTime

data class EventDto(
  val oasysOffenderPk: Long,
  val offenderPNC: String?,
  val assessmentType: String?,
  val assessmentStatus: String,
  val eventDate: LocalDateTime?,
  val eventType: EventType
) {

  companion object {
    fun from(assessment: Assessment): EventDto {
      return EventDto(
        oasysOffenderPk = assessment.group.offender.offenderPk,
        offenderPNC = assessment.group.offender.pnc,
        assessmentType = assessment.assessmentType,
        assessmentStatus = assessment.assessmentStatus,
        eventDate = assessment.dateCompleted,
        eventType = when (assessment.assessmentStatus) {
          AssessmentStatusType.COMPLETE.value -> EventType.ASSESSMENT_COMPLETED
          AssessmentStatusType.GUILLOTINED.value -> EventType.ASSESSMENT_GUILLOTINED
          else -> EventType.ASSESSMENT_COMPLETED
        }
      )
    }
  }
}

enum class EventType(val value: String) {
  ASSESSMENT_COMPLETED("ASSESSMENT_COMPLETED"),
  ASSESSMENT_GUILLOTINED("ASSESSMENT_GUILLOTINED")
}
