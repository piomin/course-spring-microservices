package pl.piomin.samples.eventdriven.cunsumerb

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pl.piomin.samples.eventdriven.cunsumerb.message.CallmeEvent
import pl.piomin.samples.eventdriven.cunsumerb.message.CallmeResponse
import java.util.function.Consumer
import java.util.function.Function

@SpringBootApplication
class ConsumerBApplication {

    val logger: Logger = LoggerFactory.getLogger(ConsumerBApplication::class.java)

//    @Bean
//    fun callmeEventConsumer(): Consumer<CallmeEvent> = Consumer { logger.info("Received: {}", it) }

    @Bean
    fun fireForget(): Consumer<CallmeEvent> = Consumer { logger.info("Received(fireForget): {}", it) }

//    @Bean
//    fun process(): Consumer<CallmeEvent> = Consumer { logger.info("Received(process): {}", it) }

    @Bean
    fun process(): Function<CallmeEvent, CallmeResponse> = Function { logAndResponse(it) }

    private fun logAndResponse(callmeEvent: CallmeEvent): CallmeResponse {
        logger.info("Received(process): {}", callmeEvent)
        return CallmeResponse(callmeEvent.id, "I'm callme response")
    }
}

fun main(args: Array<String>) {
    runApplication<ConsumerBApplication>(*args)
}