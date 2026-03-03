package pl.piomin.samples.eventdriven.producer.controller

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.piomin.samples.eventdriven.producer.message.CallmeEvent
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequestMapping("/producer")
class ProducerController(val streamBridge: StreamBridge) {

    private val id = AtomicInteger()

    @PostMapping("/{message}")
    fun sendEvent(@PathVariable message: String): Boolean {
        return streamBridge.send("callmeEventSupplier-out-0", CallmeEvent(id.incrementAndGet(), message))
    }

    @PostMapping("/{message}/process/{process}")
    fun sendEventWithHeader(@PathVariable message: String, @PathVariable process: Boolean): Boolean {
        return streamBridge.send("callmeEventSupplier-out-0",
                MessageBuilder.createMessage(CallmeEvent(id.incrementAndGet(), message),
                        MessageHeaders(mutableMapOf(Pair<String, Any>("to_process", process)))))
    }
}