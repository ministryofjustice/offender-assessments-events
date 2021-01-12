package uk.gov.justice.digital.hmpps.assessments_events
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OffenderAssessmentsEvents

fun main(args: Array<String>) {
  runApplication<OffenderAssessmentsEvents>(*args)
}
