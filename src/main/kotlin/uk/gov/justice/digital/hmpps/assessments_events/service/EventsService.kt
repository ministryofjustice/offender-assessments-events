package uk.gov.justice.digital.hmpps.assessments_events.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.dto.EventDto
import uk.gov.justice.digital.hmpps.assessments_events.repository.AssessmentRepository
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEvent
import java.time.LocalDateTime

@Service
class EventsService(val assessmentRepository: AssessmentRepository) {

    @Autowired
    lateinit var lastAccessedEvent: LastAccessedEvent

    val assessmentCompletedStatus = "COMPLETE"
    fun addNewEventsToTopic(){
        val newEvents = getNewEvents(LocalDateTime.now().minusDays())
        //TODO add to topic
    }

    fun getNewEvents(lastAccessedEvent: LocalDateTime): Collection<EventDto> {

//        val lastAccessedEvent = lastAccessedEvent.lastAccessedEvent()

        // (we only want a maximum n number of completed assessments to add to the topic at a time. This n will be configured as a property)
        val assessments = assessmentRepository.findByDateCompletedAfterAndAssessmentStatusOrderByDateCompleted(lastAccessedEvent, assessmentCompletedStatus)
        val events = assessments.map { EventDto.from(it) }

//        LastAccessedEvent.saveLastAccessedEvent(assessments.last().dateCompleted)

        return events
    }
}
