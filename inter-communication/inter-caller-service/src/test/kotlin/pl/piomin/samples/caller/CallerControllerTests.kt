package pl.piomin.samples.caller

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import pl.piomin.samples.caller.client.CallmeClient
import pl.piomin.samples.caller.model.CallmeResponse
import org.hamcrest.Matchers.containsString

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CallerControllerTests {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var restTemplate: RestTemplate

    @MockitoBean
    lateinit var callmeClient: CallmeClient

    private lateinit var mockMvc: MockMvc
    private lateinit var mockServer: MockRestServiceServer

    @BeforeAll
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun ping() {
        mockServer.expect(requestTo(containsString("/callme/call")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess("""{"id":1,"message":"FIRST"}""", MediaType.APPLICATION_JSON))

        mockMvc.post("/caller/send/{message}", "Hello").andExpect {
            status { isOk() }
            jsonPath("$.message") { value("FIRST") }
        }

        mockServer.verify()
        mockServer.reset()
    }

    @Test
    fun pingFeign() {
        whenever(callmeClient.call(any())).thenReturn(CallmeResponse(1, "FIRST"))

        mockMvc.post("/caller-with-feign/send/{message}", "Hello").andExpect {
            status { isOk() }
            jsonPath("$.message") { value("FIRST") }
        }
    }

}
