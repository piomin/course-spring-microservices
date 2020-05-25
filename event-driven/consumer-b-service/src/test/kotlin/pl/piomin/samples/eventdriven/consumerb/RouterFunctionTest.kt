package pl.piomin.samples.eventdriven.consumerb

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.messaging.support.MessageBuilder
import pl.piomin.samples.eventdriven.cunsumerb.ConsumerBApplication
import pl.piomin.samples.eventdriven.cunsumerb.message.CallmeEvent
import pl.piomin.samples.eventdriven.cunsumerb.message.CallmeResponse

@SpringBootTest(classes = [ConsumerBApplication::class])
@Import(TestChannelBinderConfiguration::class)
class RouterFunctionTest {

    @Autowired
    lateinit var inputDestination: InputDestination
    @Autowired
    lateinit var outputDestination: OutputDestination

    @Test
    fun testRouter() {
        inputDestination.send(MessageBuilder.withPayload(CallmeEvent(1, "I'm callme event"))
                .setHeader("to_process", true)
                .build())
        val response = outputDestination.receive()
        Assertions.assertNotNull(response)
        Assertions.assertTrue(response.payload.isNotEmpty())
        val payload = String(response.payload)
        val payloadObject = ObjectMapper().readValue(payload, CallmeResponse::class.java)
        Assertions.assertEquals(1, payloadObject.id)
        Assertions.assertEquals("I'm callme response", payloadObject.message)
    }

}