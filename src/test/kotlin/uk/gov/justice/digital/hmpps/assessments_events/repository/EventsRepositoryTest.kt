package uk.gov.justice.digital.hmpps.assessments_events.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.jdbc.SqlGroup
import uk.gov.justice.digital.hmpps.assessments_events.integration.IntegrationTestBase

@SqlGroup(
        Sql(scripts = ["classpath:events/before-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
        Sql(scripts = ["classpath:events/after-test.sql"], config = SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
)
class EventsRepositoryTest(@Autowired private val eventsRepository: EventsRepository) : IntegrationTestBase() {

    @Test
    fun returnsAllEventsGreaterThan(){

        val eventEntities = eventsRepository.findByEventLogPkGreaterThanAndEventTypeCodeEquals(5, "CMP_ASSMT")
        assertThat(eventEntities).extracting("eventLogPk").containsExactly(6L)
    }

    @Test
    fun returnsOnlyCompletedAssessmentEvents(){

        val eventEntities = eventsRepository.findByEventLogPkGreaterThanAndEventTypeCodeEquals(1, "CMP_ASSMT")
        assertThat(eventEntities).extracting("eventTypeCode").containsOnly("CMP_ASSMT")
    }
}