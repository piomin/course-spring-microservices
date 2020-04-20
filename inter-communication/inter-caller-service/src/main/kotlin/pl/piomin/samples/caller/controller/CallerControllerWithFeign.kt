package pl.piomin.samples.caller.controller

import org.springframework.web.bind.annotation.*
import pl.piomin.samples.caller.client.CallmeClient
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation

@RestController
@RequestMapping("/caller-with-feign")
class CallerControllerWithFeign(private val client: CallmeClient) {

    private var id: Int = 0

    @PostMapping("/send/{message}")
    fun send(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        return client.call(request)
    }

    @PostMapping("/slow-send/{message}")
    fun slowSend(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        return client.slowCall(request)
    }

    @GetMapping("/conversations")
    fun findAllConversations(): MutableList<Conversation>? =
            client.findAllConversations()

    @GetMapping("/conversations/{requestId}")
    fun findByRequestId(@PathVariable requestId: Int): Conversation? =
            client.findConversationByRequestId(requestId)
}