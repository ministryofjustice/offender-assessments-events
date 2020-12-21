package uk.gov.justice.digital.hmpps.assessments_events.service

import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventType
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.AssessmentGroup
import uk.gov.justice.digital.hmpps.assessments_events.entity.Offender
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Detail Service tests")
internal class EventServiceTest : IntegrationTestBase() {
    val assessmentRepository: AssessmentRepository = mockk()
    val service = EventsService(assessmentRepository);

    @BeforeEach
    fun resetAllMocks() {
        clearMocks(assessmentRepository)
    }

    @AfterEach
    fun verifyRepositories() {
        confirmVerified(assessmentRepository)
    }

    @Nested
    @DisplayName("Get New Events")
    inner class GetNewEventsTests {
        @Test
        fun `Should return dto if one assessment is found`() {
            val assessment = getPopulatedAssessment()
            val assessments = listOf(assessment)
            every {
                assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(
                    timeCompleted.minusDays(1),
                    "COMPLETE"
                )
            } returns assessments

            val response = service.getNewEvents(timeCompleted.minusDays(1))

            verify {
                assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(
                    timeCompleted.minusDays(1),
                    "COMPLETE"
                )
            }

            assertThat(response).hasSize(1)
            assertThat(response).isEqualTo(
                listOf(
                    EventDto(
                        offenderPk,
                        offenderPNC,
                        assessmentType,
                        assessmentStatus,
                        timeCompleted,
                        eventType
                    )
                )
            )
        }
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