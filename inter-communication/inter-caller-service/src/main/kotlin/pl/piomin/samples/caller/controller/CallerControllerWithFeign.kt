package pl.piomin.samples.caller.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import pl.piomin.samples.caller.client.CallmeClient
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation

@RestController
@RequestMapping("/caller-with-feign")
class CallerControllerWithFeign(private val client: CallmeClient) {

    private val logger: Logger = LoggerFactory.getLogger(CallerControllerWithFeign::class.java)

    private var id: Int = 0

    @PostMapping("/send/{message}")
    fun send(@PathVariable message: String): CallmeResponse? {
        logger.info("In: {}", message)
        val request = CallmeRequest(++id, message)
        return client.call(request)
    }

    @PostMapping("/slow-send/{message}")
    fun slowSend(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        return client.slowCall(request)
    }

    @PostMapping("/random-send/{message}")
    fun randomSend(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        return client.randomCall(request)
    }

    @GetMapping("/conversations")
    fun findAllConversations(): MutableList<Conversation>? =
            client.findAllConversations()

    @GetMapping("/conversations/{requestId}")
    fun findByRequestId(@PathVariable requestId: Int): Conversation? =
            client.findConversationByRequestId(requestId)
}