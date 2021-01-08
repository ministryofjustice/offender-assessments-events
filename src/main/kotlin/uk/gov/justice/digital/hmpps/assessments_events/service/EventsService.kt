package uk.gov.justice.digital.hmpps.assessments_events.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.entity.CompletedStatusType
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEventHelper
import java.time.LocalDateTime

@Service
class EventsService(val assessmentRepository: AssessmentRepository, var lastAccessedEventHelper: LastAccessedEventHelper, val snsService: SnsService) {

  fun addNewEventsToTopic() {
    val events = getNewEvents()
    snsService.sendEventSNS(events)
  }

  fun getNewEvents(): Collection<EventDto> {

    val lastAccessedEvent = getLastAccessedEventDate()
    // TODO (we only want a maximum n number of completed assessments to add to the topic at a time. This n should be configured as a property)
    val assessments = assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(lastAccessedEvent, CompletedStatusType.COMPLETE.value)
    val events = assessments.map { EventDto.from(it) }
    if (events.isEmpty()) {
      saveLastAccessedEventDate(LocalDateTime.now())
    } else { saveLastAccessedEventDate(assessments.last().dateCompleted) }
    return events
  }

  fun getLastAccessedEventDate(): LocalDateTime {
    return lastAccessedEventHelper.lastAccessedEvent()
  }

  fun saveLastAccessedEventDate(newLastAccessedEvent: LocalDateTime?) {
    if (newLastAccessedEvent == null) { throw Exception() } else
      lastAccessedEventHelper.saveLastAccessedEvent(newLastAccessedEvent)
  }
}
