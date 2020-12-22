package uk.gov.justice.digital.hmpps.assessments_events.controller
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.assessments_events.service.EventsService

@RestController
class EventsController(val eventsService: EventsService) {

    fun getNewEventsToTopic() {
        return eventsService.addNewEventsToTopic()
    }
}