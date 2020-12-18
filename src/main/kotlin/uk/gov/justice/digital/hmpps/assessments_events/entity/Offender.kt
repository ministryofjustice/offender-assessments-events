package uk.gov.justice.digital.hmpps.assessments_events.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "OFFENDER")
data class Offender (
    @Id
    @Column(name = "OFFENDER_PK")
    val offenderPk: Long,

    @Column(name = "PNC")
    val pnc: String? = null
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Offender) return false
        return offenderPk == other.offenderPk
    }

    override fun hashCode(): Int {
        return 31
    }
}