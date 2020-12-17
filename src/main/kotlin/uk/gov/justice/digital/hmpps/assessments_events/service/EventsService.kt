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

    val assessmentCompletedEventType = "CMP_ASSMT"
    fun addNewEventsToTopic(){
//        val newEvents = getNewEvents()
        //TODO add to topic
    }

//    fun getNewEvents(): Collection<EventDto> {
//
//        // TODO get the date of the last accessed Completed Assessments from local file src/main/resources/last-accessed.properties
//        // TODO LastAccessedEvent in utils is helper class (wip)
//        val lastAccessedEvent = lastAccessedEvent.lastAccessedEvent()
//
//        // DONE call repository to get the new Completed Assessments (since lastAccessed date)
//        // DONE  (we only want a maximum n number of completed assessments to add to the topic at a time. This n will be configured as a property)
//        val assessments = assessmentRepository.findFirst20ByDateCompletedAfterOrderByDateCompleted(LocalDateTime.parse(lastAccessedEvent))
//
//        // TODO For each completed assessment, convert entity to dto and send list of dtos to topic (in SnsService)
//        val events = assessments.map {
////            val offender = it.group.offender.pnc
////            EventDto(
////                it.group. ?: 1L, // TODO - what do we do if this is null?
////                it.group.offender.pnc ?: "eep", // TODO - what do we do if this is null?
////                it.assessmentType ?:"eep",  // TODO - what do we do if this is null?
////                it.createDate ?: LocalDateTime.now(), // TODO do we want created or completed date?
////                EventType.ASSESSMENT_COMPLETED // TODO how are we evaluating the event type?
////            )
//        }
//
//        // TODO: if all worked then save new last accessed date based on the successfully added completed assessment dates
//        assessments.last().dateCompleted
//
////        return events
//    }
}
