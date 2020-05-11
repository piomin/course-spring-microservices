package pl.piomin.samples.caller.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation

@FeignClient(name = "inter-callme-service", path = "/callme")
interface CallmeClient {

    @PostMapping("/call")
    fun call(@RequestBody request: CallmeRequest) : CallmeResponse

    @GetMapping("/conversations")
    fun findAllConversations(): MutableList<Conversation>

    @GetMapping("/conversations/{requestId}")
    fun findConversationByRequestId(requestId: Int): Conversation?

    @PostMapping("/slow-call")
    fun slowCall(@RequestBody request: CallmeRequest) : CallmeResponse

    @PostMapping("/random-call")
    fun randomCall(@RequestBody request: CallmeRequest) : CallmeResponse

}