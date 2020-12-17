package uk.gov.justice.digital.hmpps.assessments_events.entity

import javax.persistence.*

@Table(name = "OASYS_ASSESSMENT_GROUP")
@Entity
data class AssessmentGroup (
    @Id
    @Column(name = "OASYS_ASSESSMENT_GROUP_PK")
    val oasysAssessmentGroupPk: Long,

    @ManyToOne
    @JoinColumn(name = "OFFENDER_PK", referencedColumnName = "OFFENDER_PK")
    val offender: Offender,

    @OneToMany
    @JoinColumn(name = "OASYS_ASSESSMENT_GROUP_PK", referencedColumnName = "OASYS_ASSESSMENT_GROUP_PK")
    val assessments: Set<Assessment>

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AssessmentGroup) return false
        return oasysAssessmentGroupPk == other.oasysAssessmentGroupPk
    }

    override fun hashCode(): Int {
        return 31
    }

}
