package uk.gov.justice.digital.hmpps.assessments_events.dto

import java.time.LocalDateTime

data class EventDto (
        val oasysOffenderPk: Long,
        val offenderPNC: String,
        val assessmentType: String,
        val eventDate: LocalDateTime,
        val eventType: EventType
        ) {
        companion object{

                fun from(assessment: Assessment):EventDto{
                        return EventDto(
                                oasysOffenderPk = assessment.group.offender.offenderPk,
                                offenderPNC = assessment.group.offender.pnc,
                                assessmentType = assessment.assessmentType,
                                assessmentStatus = assessment.assessmentStatus,
                                eventDate =  assessment.dateCompleted,
                                eventType = EventType.ASSESSMENT_COMPLETED)
                }
        }
}

enum class EventType {
    ASSESSMENT_COMPLETED
}
