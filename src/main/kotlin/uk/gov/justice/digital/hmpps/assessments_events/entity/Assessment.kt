package uk.gov.justice.digital.hmpps.assessments_events.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "OASYS_SET")
data class Assessment(
        @Id
        @Column(name = "OASYS_SET_PK")
        val oasysSetPk: Long,

        @Column(name = "ASSESSMENT_STATUS_ELM")
        val assessmentStatus: String,

        @Column(name = "ASSESSMENT_TYPE_ELM")
        val assessmentType: String? = null,

        @Column(name = "DATE_COMPLETED")
        val dateCompleted: LocalDateTime? = null,

        @ManyToOne
        @JoinColumn(name = "OASYS_ASSESSMENT_GROUP_PK")
        val group: AssessmentGroup,
):Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Assessment) return false
        return oasysSetPk == other.oasysSetPk
    }

    override fun hashCode(): Int {
        return 31
    }
}
