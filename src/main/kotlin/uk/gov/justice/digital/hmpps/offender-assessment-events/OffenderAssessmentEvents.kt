package uk.gov.justice.digital.hmpps.hmppstemplatepackagename

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class OffenderAssessmentEvents

fun main(args: Array<String>) {
  runApplication<OffenderAssessmentEvents>(*args)
}
