plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "3.1.6"
  kotlin("plugin.spring") version "1.4.30"
  kotlin("plugin.jpa") version "1.4.30"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.springframework.data:spring-data-relational:2.2.0")

  implementation("org.springframework.cloud:spring-cloud-starter-aws-messaging:2.2.6.RELEASE")

  implementation("org.springdoc:springdoc-openapi-ui:1.5.8")
  implementation("org.springdoc:springdoc-openapi-data-rest:1.5.8")
  implementation("org.springdoc:springdoc-openapi-kotlin:1.5.8")

  implementation("org.apache.commons:commons-lang3:3.12.0")

  implementation(files("lib/ojdbc8-12.2.0.1.jar"))

  runtimeOnly("com.h2database:h2:1.4.200")
  runtimeOnly("org.flywaydb:flyway-core:7.8.1")

  testImplementation("io.jsonwebtoken:jjwt:0.9.1")
  testImplementation("com.ninja-squad:springmockk:3.0.1")
  testImplementation("com.github.tomakehurst:wiremock-standalone:2.27.2")
  testImplementation("org.testcontainers:testcontainers:1.15.3")
  testImplementation("org.testcontainers:junit-jupiter:1.15.3")
  testImplementation("org.testcontainers:localstack:1.15.3")
}
