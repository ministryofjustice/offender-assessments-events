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
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEventHelper
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Detail Service tests")
internal class EventServiceTest : IntegrationTestBase() {

    private final val assessmentRepository: AssessmentRepository = mockk()
    private final val lastAccessedEventHelper: LastAccessedEventHelper = mockk()
    private final val snsService: SnsService = mockk()


    val service = EventsService(assessmentRepository, lastAccessedEventHelper, snsService)

    @BeforeEach
    fun resetAllMocks() {
        clearMocks(assessmentRepository)
        every {lastAccessedEventHelper.lastAccessedEvent()} returns timeCompleted.minusDays(1)
        every {lastAccessedEventHelper.saveLastAccessedEvent(any())} just Runs
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
            val assessments = listOf(getPopulatedAssessment())
            every {
                assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(
                    timeCompleted.minusDays(1),
                    "COMPLETE"
                ) } returns assessments

            val response = service.getNewEvents()

            verify {
                assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(
                    timeCompleted.minusDays(1),
                    "COMPLETE") }
            assertThat(response).hasSize(1)
            assertThat(response).isEqualTo(listOf( getPopulatedDto()))
        }

        @Test
        fun `Should return multiple dtos if multiple assessments found`() {
            val assessment = getPopulatedAssessment()
            val assessments = listOf(
                assessment,
                assessment,
                assessment,
                assessment
            )
            every {
                assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(
                    timeCompleted.minusDays(1),
                    "COMPLETE"
                ) } returns assessments

            val response = service.getNewEvents()

            verify {
                assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(
                    timeCompleted.minusDays(1),
                    "COMPLETE") }
            assertThat(response).hasSize(4)
            assertThat(response).element(3).isEqualTo(getPopulatedDto())
        }
    }

    companion object {
        private val eventType = EventType.ASSESSMENT_COMPLETED
        private const val setPk = 4L
        private const val offenderPk = 25L
        private const val offenderPNC = "ABC"
        private const val assessmentType = "magic"
        private const val assessmentStatus = "pending"
        val timeCompleted: LocalDateTime = LocalDateTime.now()

        fun getPopulatedDto(): EventDto {
            return EventDto(
                offenderPk,
                offenderPNC,
                assessmentType,
                assessmentStatus,
                timeCompleted,
                eventType
            )
        }

        fun getPopulatedAssessment(): Assessment {
            return Assessment(
                setPk,
                assessmentStatus,
                assessmentType,
                timeCompleted,
                AssessmentGroup(
                    7L,
                    Offender(
                        offenderPk,
                        offenderPNC
                    )
                )
            )
        }
    }
}