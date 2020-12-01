package uk.gov.justice.digital.hmpps.assessments_events.config

import com.microsoft.applicationinsights.TelemetryClient
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.*
import org.springframework.core.type.AnnotatedTypeMetadata

@Configuration
class ApplicationInsightsConfiguration {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Bean
    @Conditional(AppInsightKeyAbsentCondition::class)
    fun telemetryClient(): TelemetryClient {
        return TelemetryClient()
    }

    class AppInsightKeyAbsentCondition : Condition {
        override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
            val telemetryKey = context.environment.getProperty("appinsights.instrumentationkey")
            if (StringUtils.isBlank(telemetryKey)) {
                log.warn("Application Insights Implementation Key is blank")
            }
            return StringUtils.isBlank(telemetryKey)
        }
    }
}