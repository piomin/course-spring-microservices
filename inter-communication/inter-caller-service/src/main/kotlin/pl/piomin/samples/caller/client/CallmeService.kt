package pl.piomin.samples.caller.client

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation

@HttpExchange
interface CallmeService {

    @PostExchange("/call")
    fun call(@RequestBody request: CallmeRequest): CallmeResponse

    @GetExchange("/conversations")
    fun findAllConversations(): MutableList<Conversation>

    @GetExchange("/conversations/{requestId}")
    fun findConversationByRequestId(@PathVariable requestId: Int): Conversation?
}
