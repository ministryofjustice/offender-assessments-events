package uk.gov.justice.digital.hmpps.assessments_events
import org.springframework.web.bind.annotation.RestController

@RestController
class EventsController(val eventsService: EventsService) {

    fun getNewEventsToTopic() {
        return eventsService.addNewEventsToTopic()
    }

}