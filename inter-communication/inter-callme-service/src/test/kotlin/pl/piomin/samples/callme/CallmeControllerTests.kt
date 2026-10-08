package pl.piomin.samples.callme

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.piomin.samples.callme.repository.ConversationRepository

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CallmeControllerTests {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var repository: ConversationRepository

    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    fun call() {
        mockMvc.post("/callme/call") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"id":1,"message":"ABC"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("CBA") }
        }

        assertNotNull(repository.findByRequestId(1))
    }

    @Test
    fun slowCall() {
        mockMvc.post("/callme/slow-call") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"id":2,"message":"ABC"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.message") { value("CBA") }
        }

        assertNotNull(repository.findByRequestId(2))
    }

}
