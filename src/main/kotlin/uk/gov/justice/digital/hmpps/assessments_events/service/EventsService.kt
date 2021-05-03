package uk.gov.justice.digital.hmpps.assessments_events.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.AssessmentStatusType
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEventHelper
import java.time.LocalDateTime

@Service
class EventsService(
  val assessmentRepository: AssessmentRepository,
  var lastAccessedEventHelper: LastAccessedEventHelper,
  val snsService: SnsService
) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun sendNewEventsToTopic() {
    val events = getNewAssessmentEvents()
    snsService.sendEventSNS(dtoFromEntity(events))
  }

  fun sendNewEventsToTopic(date: LocalDateTime) {
    val events = getNewAssessmentEventsSinceDate(date)
    snsService.sendEventSNS(dtoFromEntity(events))
  }

  fun getNewAssessmentEvents(): Collection<Assessment> {
    val lastAccessedEvent = getLastAccessedEventDate()
    val assessments = getNewAssessmentEventsSinceDate(lastAccessedEvent)
    saveLastAccessedEventDate(assessments)
    return assessments
  }

  fun getNewAssessmentEventsSinceDate(date: LocalDateTime): Collection<Assessment> {
    log.info("Getting new events since date: $date")
    val assessments = assessmentRepository.findByDateCompletedAfterAndAssessmentStatusIn(
      date,
      setOf(AssessmentStatusType.COMPLETE.value, AssessmentStatusType.GUILLOTINED.value)
    )
    log.info("Found ${assessments.size} new events.")
    return assessments.sortedBy { it.dateCompleted }
  }

  fun getLastAccessedEventDate(): LocalDateTime {
    val date = lastAccessedEventHelper.lastAccessedEvent()
    log.info("Using last accessed event date: $date")
    return date
  }

  fun saveLastAccessedEventDate(eventDtos: Collection<Assessment>) {
    if (eventDtos.isNotEmpty()) {
      val newLastAccessedEventDate = eventDtos.last().dateCompleted
      log.info("Saving new last accessed event date to: $newLastAccessedEventDate")
      lastAccessedEventHelper.saveLastAccessedEvent(newLastAccessedEventDate!!)
    } else {
      log.info("No new last accessed event date saved")
    }
  }

  fun dtoFromEntity(assessments: Collection<Assessment>): List<EventDto> {
    return assessments.map { EventDto.from(it) }
  }
}
