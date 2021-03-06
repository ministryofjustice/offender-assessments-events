package uk.gov.justice.digital.hmpps.assessments_events.service

import io.mockk.Called
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventType
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.AssessmentGroup
import uk.gov.justice.digital.hmpps.assessments_events.entity.Offender
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEventHelper
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DisplayName("Event Service tests")
internal class EventServiceTest {

  private val assessmentRepository: AssessmentRepository = mockk()
  private val lastAccessedEventHelper: LastAccessedEventHelper = mockk()
  private val snsService: SnsService = mockk()

  private val eventsService = EventsService(assessmentRepository, lastAccessedEventHelper, snsService)

  private val dateCompleted: LocalDateTime = LocalDateTime.of(2020, 1, 1, 1, 1, 1)
  private val testDate: LocalDateTime = dateCompleted.minusDays(1)

  @BeforeEach
  fun resetAllMocks() {
    clearMocks(assessmentRepository)
    every { lastAccessedEventHelper.lastAccessedEvent() } returns testDate
    every { lastAccessedEventHelper.saveLastAccessedEvent(any()) } just Runs
    every { snsService.sendEventSNS(any()) } just Runs
  }

  @AfterEach
  fun verifyRepositories() {
    confirmVerified(assessmentRepository)
  }

  @Nested
  @DisplayName("Get New Events")
  inner class GetNewEventsTests {
    @Test
    fun `Should send dtos to sns if two assessments are found`() {
      val assessments = listOf(completedAssessment(), lockedIncompleteAssessment())

      every {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      } returns assessments

      eventsService.sendNewEventsToTopic()

      verify {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      }
      val eventsDto = slot<Collection<EventDto>>()
      verify(exactly = 1) { snsService.sendEventSNS(capture(eventsDto)) }
      assertThat(eventsDto.captured).hasSize(2)
      assertThat(eventsDto.captured.elementAt(0)).isEqualTo(completedAssessmentDto())
      assertThat(eventsDto.captured.elementAt(1)).isEqualTo(lockedIncompleteAssessmentDto())
    }

    @Test
    fun `Should update last accessed event when assessment is found`() {
      val assessments = listOf(completedAssessment())

      every {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      } returns assessments

      eventsService.sendNewEventsToTopic()

      verify {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      }
      val eventsDto = slot<Collection<EventDto>>()
      verify(exactly = 1) { snsService.sendEventSNS(capture(eventsDto)) }
      verify(exactly = 1) { lastAccessedEventHelper.saveLastAccessedEvent(dateCompleted) }
    }

    @Test
    fun `Should send multiple event dtos to SNS if multiple assessments found`() {
      val assessment = completedAssessment()
      val assessments = listOf(assessment, assessment, assessment, assessment)

      every {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      } returns assessments

      eventsService.sendNewEventsToTopic()

      verify(exactly = 1) {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      }
      val eventsDto = slot<Collection<EventDto>>()
      verify(exactly = 1) { snsService.sendEventSNS(capture(eventsDto)) }
      assertThat(eventsDto.captured).hasSize(4)
      assertThat(eventsDto.captured).element(3).isEqualTo(completedAssessmentDto())
    }

    @Test
    fun `Should update last accessed date with most recent date if multiple assessments found`() {
      val assessment = completedAssessment()
      val assessments = listOf(
        assessment,
        assessment.copy(dateCompleted = dateCompleted.minusDays(3)),
        assessment.copy(dateCompleted = dateCompleted.minusDays(5)),
        assessment.copy(dateCompleted = dateCompleted.minusHours(9))
      )
      every {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      } returns assessments

      eventsService.sendNewEventsToTopic()

      verify(exactly = 1) {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(testDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      }
      verify(exactly = 1) { lastAccessedEventHelper.saveLastAccessedEvent(dateCompleted) }
    }

    @Test
    fun `Should send event dtos since given date to sns`() {
      val sinceDate = LocalDateTime.of(2021, 1, 1, 1, 1, 1)

      val assessments = listOf(completedAssessment())
      every {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(sinceDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      } returns assessments
      eventsService.sendNewEventsToTopic(sinceDate)

      verify(exactly = 1) {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(
          sinceDate,
          setOf("COMPLETE", "LOCKED_INCOMPLETE")
        )
      }
      verify(exactly = 1) { snsService.sendEventSNS(any()) }

      val eventsDto = slot<Collection<EventDto>>()
      verify(exactly = 1) { snsService.sendEventSNS(capture(eventsDto)) }
      assertThat(eventsDto.captured).hasSize(1)
      assertThat(eventsDto.captured).element(0).isEqualTo(completedAssessmentDto())
    }

    @Test
    fun `Should not update last accessed event when given date`() {
      val sinceDate = LocalDateTime.of(2021, 1, 1, 1, 1, 1)

      val assessments = listOf(completedAssessment())
      every {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(sinceDate, setOf("COMPLETE", "LOCKED_INCOMPLETE"))
      } returns assessments
      eventsService.sendNewEventsToTopic(sinceDate)

      verify(exactly = 1) {
        assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(
          sinceDate,
          setOf("COMPLETE", "LOCKED_INCOMPLETE")
        )
      }
      verify { lastAccessedEventHelper wasNot Called }
    }
  }

  private val eventType = EventType.ASSESSMENT_COMPLETED
  private val setPk = 4L
  private val offenderPk = 25L
  private val offenderPNC = "ABC"
  private val assessmentType = "type"
  private val assessmentStatus = "COMPLETE"

  fun completedAssessmentDto(): EventDto {
    return EventDto(
      offenderPk,
      offenderPNC,
      assessmentType,
      assessmentStatus,
      dateCompleted,
      eventType
    )
  }

  fun lockedIncompleteAssessmentDto(): EventDto {
    return completedAssessmentDto().copy(
      assessmentStatus = "LOCKED_INCOMPLETE",
      eventType = EventType.ASSESSMENT_LOCKED_INCOMPLETE,
      eventDate = lockedIncompleteAssessment().dateCompleted
    )
  }

  fun completedAssessment(): Assessment {
    return Assessment(
      setPk,
      assessmentStatus,
      assessmentType,
      dateCompleted,
      AssessmentGroup(
        7L,
        Offender(
          offenderPk,
          offenderPNC
        )
      )
    )
  }

  fun lockedIncompleteAssessment(): Assessment {
    return completedAssessment().copy(
      assessmentStatus = "LOCKED_INCOMPLETE",
      dateCompleted = LocalDateTime.of(2020, 2, 1, 1, 1, 1)
    )
  }
}
