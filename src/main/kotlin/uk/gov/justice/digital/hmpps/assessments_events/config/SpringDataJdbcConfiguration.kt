package uk.gov.justice.digital.hmpps.assessments_events.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.relational.core.dialect.Dialect
import org.springframework.data.relational.core.dialect.OracleDialect
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations

/* This is required because the current implementation of Spring JDBC does not support Oracle dialect
* https://spring.io/blog/2020/05/20/migrating-to-spring-data-jdbc-2-0 */
@Configuration
@Profile("oracle", "test")
class SpringDataJdbcConfiguration : AbstractJdbcConfiguration() {
  override fun jdbcDialect(operations: NamedParameterJdbcOperations): Dialect {
    return OracleDialect.INSTANCE
  }
}
