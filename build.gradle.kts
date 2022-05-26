plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "4.2.0-beta-4"
  kotlin("plugin.spring") version "1.6.21"
  kotlin("plugin.jpa") version "1.6.21"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencyCheck {
  suppressionFiles.add("suppressions.xml")
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
  implementation("org.springframework.data:spring-data-relational")

  implementation("org.springframework.cloud:spring-cloud-starter-aws-messaging:2.2.6.RELEASE")

  implementation("org.springdoc:springdoc-openapi-ui:1.6.7")
  implementation("org.springdoc:springdoc-openapi-data-rest:1.6.7")
  implementation("org.springdoc:springdoc-openapi-kotlin:1.6.7")

  implementation("org.apache.commons:commons-lang3:3.12.0")

  implementation("com.oracle.database.jdbc:ojdbc8:21.5.0.0")
  implementation("net.logstash.logback:logstash-logback-encoder:7.1.1")
  runtimeOnly("com.h2database:h2:2.1.212")
  runtimeOnly("org.flywaydb:flyway-core:8.5.9")

  testImplementation("io.jsonwebtoken:jjwt:0.9.1")
  testImplementation("com.ninja-squad:springmockk:3.1.1")
  testImplementation("com.github.tomakehurst:wiremock-standalone:2.27.2")
  testImplementation("org.testcontainers:junit-jupiter:1.17.1")
  testImplementation("org.testcontainers:localstack:1.17.1")
  testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
}
repositories {
  mavenCentral()
}
