package pl.piomin.samples.caller.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import pl.piomin.samples.caller.client.CallmeService
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequestMapping("/caller-with-http-service")
class CallerControllerWithHttpService(private val client: CallmeService) {

    private val logger: Logger = LoggerFactory.getLogger(CallerControllerWithHttpService::class.java)

    private val id = AtomicInteger()

    @PostMapping("/send/{message}")
    fun send(@PathVariable message: String): CallmeResponse? {
        logger.info("In: {}", message)
        val request = CallmeRequest(id.incrementAndGet(), message)
        return client.call(request)
    }

    @GetMapping("/conversations")
    fun findAllConversations(): MutableList<Conversation>? = client.findAllConversations()

    @GetMapping("/conversations/{requestId}")
    fun findByRequestId(@PathVariable requestId: Int): Conversation? =
        client.findConversationByRequestId(requestId)
}
