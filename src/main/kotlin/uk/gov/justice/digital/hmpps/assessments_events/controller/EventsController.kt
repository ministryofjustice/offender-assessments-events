package uk.gov.justice.digital.hmpps.assessments_events.controller
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.assessments_events.service.EventsService
import java.time.LocalDateTime

@RestController
class EventsController(val eventsService: EventsService) {

  @RequestMapping(path = ["/events"], method = [RequestMethod.POST])
  @Operation(description = "Triggers the fetching of new events from OASys and then puts them on a SNS topic")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "401", description = "Invalid JWT Token"),
      ApiResponse(responseCode = "200", description = "OK")
    ]
  )
  fun getNewEventsToTopic() {
    return eventsService.sendNewEventsToTopic()
  }

  @RequestMapping(path = ["/events/date/{date}"], method = [RequestMethod.POST])
  @Operation(description = "Triggers the fetching of new events since the given date from OASys and then puts them on a SNS topic")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "401", description = "Invalid JWT Token"),
      ApiResponse(responseCode = "200", description = "OK")
    ]
  )
  fun getNewEventsSinceDateToTopic(@PathVariable date: LocalDateTime) {
    return eventsService.sendNewEventsToTopic(date)
  }
}
