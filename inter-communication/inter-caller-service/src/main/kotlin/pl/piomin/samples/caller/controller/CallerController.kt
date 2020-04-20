package pl.piomin.samples.caller.controller

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation

@RestController
@RequestMapping("/caller")
class CallerController(private val template: RestTemplate, private val factory: Resilience4JCircuitBreakerFactory) {

    private var id: Int = 0

    @PostMapping("/send/{message}")
    fun send(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        return template.postForObject("http://inter-callme-service/callme/call",
                request, CallmeResponse::class.java)
    }

    @PostMapping("/random-send/{message}")
    fun randomSend(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        val circuit = factory.create("random-circuit")
        return circuit.run { template.postForObject("http://inter-callme-service/callme/random-call",
                request, CallmeResponse::class.java) }
    }

    @PostMapping("/slow-send/{message}")
    fun slowSend(@PathVariable message: String): CallmeResponse? {
        val request = CallmeRequest(++id, message)
        return template.postForObject("http://inter-callme-service/callme/slow-call",
                request, CallmeResponse::class.java)
    }

    @GetMapping("/conversations")
    fun findAllConversations(): Array<Conversation>? =
            template.getForObject("http://inter-callme-service/callme/conversations",
                    Array<Conversation>::class.java)

    @GetMapping("/conversations/{requestId}")
    fun findByRequestId(@PathVariable requestId: Int): Conversation? =
            template.getForObject("http://inter-callme-service/callme/conversations/{requestId}",
                    Conversation::class.java, requestId)
}