package uk.gov.justice.digital.hmpps.assessments_events

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.assessments_events.repository.EventsRepository
import uk.gov.justice.digital.hmpps.assessments_events.utils.LastAccessedEvent

@Service
class EventsService(val eventsRepository: EventsRepository ) {

    @Autowired
    lateinit var lastAccessedEvent: LastAccessedEvent

    val assessmentCompletedEventType = "CMP_ASSMT"
    fun addNewEventsToTopic(){
        val newEvents = getNewEvents()
        //add to topic
    }

    fun getNewEvents(): Collection<EventDto> {

        // get the date of the last accessed Completed Assessments from local file src/main/resources/last-accessed.properties
        // LastAccessedEvent in utils is helper class (wip)
        val lastAccessedEvent = lastAccessedEvent.lastAccessedEvent()

        //call repository to get the new Completed Assessments (since lastAccessed date)
        eventsRepository.findByEventLogPkGreaterThanAndEventTypeCodeEquals(lastAccessedEvent as Long, assessmentCompletedEventType)

        // (we only want a maximum n number of completed assessments to add to the topic at a time. This n will be configured as a property)
        // For each completed assessment, convert entity to dto and send list of dtos to topic (in SnsService)
        // if all worked then save new last accessed date based on the successfully added completed assessment dates
        return listOf()
    }
}
