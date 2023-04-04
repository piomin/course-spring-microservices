package pl.piomin.samples.callme

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import pl.piomin.samples.callme.model.CallmeRequest
import pl.piomin.samples.callme.model.CallmeResponse
import pl.piomin.samples.callme.repository.ConversationRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CallmeControllerTests {

    @Autowired private lateinit var template: TestRestTemplate
    @Autowired private lateinit var repository: ConversationRepository

    @Test
    fun call() {
        val res = template.postForObject("/callme/call", CallmeRequest(1, "ABC"), CallmeResponse::class.java)
        assertNotNull(res)
        assertNotNull(res!!.message)
        assertEquals("CBA", res.message)
        assertNotNull(repository.findByRequestId(1))
    }

    @Test
    fun slowCall() {
        val res = template.postForObject("/callme/slow-call", CallmeRequest(2, "ABC"), CallmeResponse::class.java)
        assertNotNull(res)
        assertNotNull(res!!.message)
        assertEquals("CBA", res.message)
        assertNotNull(repository.findByRequestId(2))
    }

}