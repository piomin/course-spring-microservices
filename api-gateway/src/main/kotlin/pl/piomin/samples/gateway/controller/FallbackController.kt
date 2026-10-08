package pl.piomin.samples.gateway.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.piomin.samples.gateway.model.GatewayResponse
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequestMapping("/fallback")
class FallbackController {

    private val id = AtomicInteger()

    @PostMapping("/test")
    fun fallback(): Mono<GatewayResponse> = Mono.just(GatewayResponse(id.incrementAndGet(), "I'm fallback!"))
}