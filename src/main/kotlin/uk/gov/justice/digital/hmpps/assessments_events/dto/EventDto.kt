package uk.gov.justice.digital.hmpps.assessments_events.dto

import java.time.LocalDateTime

data class EventDto (
        val oasysOffenderPk: Long,
        val offenderPNC: String,
        val assessmentType: String,
        val eventDate: LocalDateTime,
        val eventType: EventType
)

enum class EventType {
    ASSESSMENT_COMPLETED
}
