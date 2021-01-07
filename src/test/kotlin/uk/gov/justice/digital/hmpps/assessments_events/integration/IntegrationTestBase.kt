package uk.gov.justice.digital.hmpps.assessments_events.integration
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.assessments_events.JwtAuthHelper
import java.time.Duration

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  internal lateinit var jwtHelper: JwtAuthHelper

  companion object {
    internal val oauthMockServer = OAuthMockServer()

    @BeforeAll
    @JvmStatic
    fun startMocks() {
      oauthMockServer.start()
    }

    @AfterAll
    @JvmStatic
    fun stopMocks() {
      oauthMockServer.stop()
    }
  }

  init {
    SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("user", "pw")
    // Resolves an issue where Wiremock keeps previous sockets open from other tests causing connection resets
    System.setProperty("http.keepAlive", "false")
  }

  @BeforeEach
  fun resetStubs() {
    oauthMockServer.resetAll()
    oauthMockServer.stubGrantToken()
  }

  internal fun setAuthorisation(user: String = "offender-assessment-api", roles: List<String> = listOf()): (HttpHeaders) -> Unit {
    val token = jwtHelper.createJwt(
      subject = user,
      scope = listOf("read"),
      expiryTime = Duration.ofHours(1L),
      roles = roles
    )
    return { it.set(HttpHeaders.AUTHORIZATION, "Bearer $token") }
  }
}
