package pl.piomin.samples.eventdriven.producer

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import pl.piomin.samples.eventdriven.producer.message.CallmeEvent
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier

@SpringBootApplication
class ProductionApplication {

    private val id = AtomicInteger()

    @Value("\${callme.supplier.enabled}")
    val supplierEnabled: Boolean = false

    @Bean
    fun callmeEventSupplier(): Supplier<Message<CallmeEvent>?> = Supplier { createEvent() }

    private fun createEvent(): Message<CallmeEvent>? {
        return if (supplierEnabled)
             MessageBuilder.withPayload(CallmeEvent(id.incrementAndGet(), "I'm callme event!"))
                     .setHeader("to_process", true)
                     .build()
        else
            null
    }
}

fun main(args: Array<String>) {
    runApplication<ProductionApplication>(*args)
}