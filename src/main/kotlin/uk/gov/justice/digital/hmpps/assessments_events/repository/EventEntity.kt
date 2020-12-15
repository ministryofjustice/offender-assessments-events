package uk.gov.justice.digital.hmpps.assessments_events.repository

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "EVENT_LOG")
class EventEntity (

        @Id
        @Column(name = "EVENT_LOG_PK")
        val eventLogPk: Long,
        @Column(name = "EVENT_TYPE_CODE")
        val eventTypeCode: String,
        @Column(name = "EVENT_TEXT")
        val eventText: String,
        @Column(name = "EVENT_DATE")
        val eventDate: LocalDateTime
        )
