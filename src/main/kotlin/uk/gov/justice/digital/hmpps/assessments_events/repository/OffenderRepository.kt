package uk.gov.justice.digital.hmpps.assessments_events.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.assessments_events.entity.Offender

@Repository
interface OffenderRepository : JpaRepository<Offender, Long> {
    fun findByOffenderPk(offenderPk: Long): Offender
}