plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "1.0.6"
  kotlin("plugin.spring") version "1.4.10"
  kotlin("plugin.jpa") version "1.3.72"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

  implementation("org.springdoc:springdoc-openapi-ui:1.4.7")
  implementation("org.springdoc:springdoc-openapi-data-rest:1.4.7")
  implementation("org.springdoc:springdoc-openapi-kotlin:1.4.7")

  implementation("org.apache.commons:commons-lang3:3.11")

  runtimeOnly("com.h2database:h2:1.4.200")
  runtimeOnly("org.flywaydb:flyway-core:6.3.3")

  testRuntimeOnly("com.h2database:h2:1.4.200")

  testImplementation("io.jsonwebtoken:jjwt:0.9.1")

}
