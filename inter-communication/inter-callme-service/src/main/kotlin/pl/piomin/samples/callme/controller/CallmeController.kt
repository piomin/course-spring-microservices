package pl.piomin.samples.callme.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import pl.piomin.samples.callme.model.CallmeRequest
import pl.piomin.samples.callme.model.CallmeResponse
import pl.piomin.samples.callme.model.Conversation
import pl.piomin.samples.callme.repository.ConversationRepository
import kotlin.random.Random

@RestController
@RequestMapping("/callme")
class CallmeController(private val repository: ConversationRepository) {

    private val logger: Logger = LoggerFactory.getLogger(CallmeController::class.java)

    @Value("\${app.delay}")
    val delay: Long = 0

    @PostMapping("/call")
    fun call(@RequestBody request: CallmeRequest) : CallmeResponse {
        val response = CallmeResponse(request.id, request.message.reversed())
        repository.add(Conversation(request = request, response = response))
        return response
    }

    @PostMapping("/random-call")
    fun randomCall(@RequestBody request: CallmeRequest) : CallmeResponse {
        var r: Long = 0
        if (delay != 0L) {
            r = Random.nextLong(delay)
            Thread.sleep(r)
        }
        logger.info("Req: message->{}, delay->{}", request.message, r)
        val response = CallmeResponse(request.id, request.message.reversed())
        repository.add(Conversation(request = request, response = response))
        return response
    }

    @PostMapping("/slow-call")
    fun slowCall(@RequestBody request: CallmeRequest) : CallmeResponse {
        Thread.sleep(1000)
        val response = CallmeResponse(request.id, request.message.reversed())
        repository.add(Conversation(request = request, response = response))
        return response
    }

    @GetMapping("/conversations")
    fun findAllConversations(): MutableList<Conversation> = repository.findAll()

    @GetMapping("/conversations/{requestId}")
    fun findConversationByRequestId(requestId: Int): Conversation? = repository.findByRequestId(requestId)

}