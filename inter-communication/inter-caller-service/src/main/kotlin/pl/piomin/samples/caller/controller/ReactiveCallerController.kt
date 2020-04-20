package pl.piomin.samples.caller.controller

import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import pl.piomin.samples.caller.model.CallmeRequest
import pl.piomin.samples.caller.model.CallmeResponse
import pl.piomin.samples.caller.model.Conversation
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactivecaller")
class ReactiveCallerController(private val clientBuilder: WebClient.Builder) {

    private var id: Int = 0

    @PostMapping("/send/{message}")
    fun send(@PathVariable message: String): Mono<CallmeResponse> {
        val request = CallmeRequest(++id, message)
        return clientBuilder.build().post().uri("http://inter-callme-service/callme/call")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CallmeResponse::class.java)
    }

    @PostMapping("/slow-send/{message}")
    fun slowSend(@PathVariable message: String): Mono<CallmeResponse> {
        val request = CallmeRequest(++id, message)
        return clientBuilder.build().post().uri("http://inter-callme-service/callme/slow-call")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CallmeResponse::class.java)
    }

    @GetMapping("/conversations")
    fun findAllConversations(): Flux<Conversation> =
            clientBuilder.build().get().uri("http://inter-callme-service/callme/conversations")
                    .retrieve()
                    .bodyToFlux(Conversation::class.java)
}