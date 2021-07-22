package pl.piomin.samples.eventdriven.producer

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import pl.piomin.samples.eventdriven.producer.message.CallmeEvent
import java.util.function.Supplier
import org.springframework.cloud.schema.registry.client.ConfluentSchemaRegistryClient

import org.springframework.cloud.schema.registry.client.SchemaRegistryClient
import org.springframework.context.annotation.Primary


@SpringBootApplication
@EnableSchemaRegistryClient
class ProductionApplication {

    var id: Int = 0

    @Value("\${callme.supplier.enabled}")
    val supplierEnabled: Boolean = false

    @Bean
    fun callmeEventSupplier(): Supplier<Message<CallmeEvent>?> = Supplier { createEvent() }

    @Primary
    @Bean
    fun schemaRegistryClient(@Value("\${spring.cloud.schemaRegistryClient.endpoint}") endpoint: String?): SchemaRegistryClient {
        val client = ConfluentSchemaRegistryClient()
        client.setEndpoint(endpoint)
        return client
    }

    private fun createEvent(): Message<CallmeEvent>? {
        return if (supplierEnabled)
             MessageBuilder.withPayload(CallmeEvent(++id, "I'm callme event!", "ping"))
                     .setHeader("to_process", true)
                     .build()
        else
            null
    }
}

fun main(args: Array<String>) {
    runApplication<ProductionApplication>(*args)
}