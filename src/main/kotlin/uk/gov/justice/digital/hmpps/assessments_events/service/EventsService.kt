package uk.gov.justice.digital.hmpps.assessments_events.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.entity.Assessment
import uk.gov.justice.digital.hmpps.assessments_events.entity.CompletedStatusType
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEventHelper
import java.time.LocalDateTime

@Service
class EventsService(val assessmentRepository: AssessmentRepository, var lastAccessedEventHelper: LastAccessedEventHelper, val snsService: SnsService) {

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  fun addNewEventsToTopic() {
    val events = getNewEvents()
    snsService.sendEventSNS(events)
  }

  fun getNewEvents(): Collection<EventDto> {

    val lastAccessedEvent = getLastAccessedEventDate()
    // TODO (we only want a maximum n number of completed assessments to add to the topic at a time. This n should be configured as a property)
    log.info("Getting new events since date: $lastAccessedEvent")

    val eventEntity = assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(lastAccessedEvent, CompletedStatusType.COMPLETE.value)
    log.info("Found ${eventEntity.size} new events.")
    val eventDtos = eventEntity.map { EventDto.from(it) }
    saveLastAccessedEventDate(eventEntity)

    return eventDtos
  }

  fun getLastAccessedEventDate(): LocalDateTime {
    return lastAccessedEventHelper.lastAccessedEvent()
  }

  fun saveLastAccessedEventDate(eventDtos: Collection<Assessment>) {
    var newLastAccessedEventDate = LocalDateTime.now()
    if (eventDtos.isNotEmpty()) { newLastAccessedEventDate = eventDtos.last().dateCompleted }
    log.info("Saving new last accessed event date to: $newLastAccessedEventDate")
    lastAccessedEventHelper.saveLastAccessedEvent(newLastAccessedEventDate)
  }
}
