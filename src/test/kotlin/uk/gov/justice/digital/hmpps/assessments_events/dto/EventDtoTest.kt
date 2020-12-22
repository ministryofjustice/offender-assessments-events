package uk.gov.justice.digital.hmpps.assessments_events.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.AssessmentGroup
import uk.gov.justice.digital.hmpps.assessments_events.entity.Offender
import java.time.LocalDateTime

class EventDtoTest {

    @Test
    fun `Create Event Dto`() {
        val eventDto = EventDto(
            offenderPk,
            offenderPNC,
            assessmentType,
            assessmentStatus,
            timeCompleted,
            eventType
        )

        assertThat(eventDto.oasysOffenderPk).isEqualTo(offenderPk)
        assertThat(eventDto.offenderPNC).isEqualTo(offenderPNC)
        assertThat(eventDto.assessmentType).isEqualTo(assessmentType)
        assertThat(eventDto.assessmentStatus).isEqualTo(assessmentStatus)
        assertThat(eventDto.eventDate).isEqualTo(timeCompleted)
        assertThat(eventDto.eventType).isEqualTo(eventType)
    }

    @Test
    fun `Create Event Dto from Assessment`() {
        val assessment = getPopulatedAssessment()

        val eventDto = EventDto.from(assessment)

        assertThat(eventDto.oasysOffenderPk).isEqualTo(offenderPk)
        assertThat(eventDto.offenderPNC).isEqualTo(offenderPNC)
        assertThat(eventDto.assessmentType).isEqualTo(assessmentType)
        assertThat(eventDto.assessmentStatus).isEqualTo(assessmentStatus)
        assertThat(eventDto.eventDate).isEqualTo(timeCompleted)
        assertThat(eventDto.eventType).isEqualTo(eventType)
    }

    companion object {
        val eventType = EventType.ASSESSMENT_COMPLETED
        private const val setPk = 4L
        const val offenderPk = 25L
        const val offenderPNC = "ABC"
        const val assessmentType = "magic"
        const val assessmentStatus = "pending"
        val timeCompleted: LocalDateTime = LocalDateTime.now()

        fun getPopulatedAssessment(): Assessment {

            val offender = Offender(
                offenderPk,
                offenderPNC,
            )
            val group = AssessmentGroup(
                7L,
                offender
            )

            return Assessment(
                setPk,
                assessmentStatus,
                assessmentType,
                timeCompleted,
                group
            )
        }
    }
}