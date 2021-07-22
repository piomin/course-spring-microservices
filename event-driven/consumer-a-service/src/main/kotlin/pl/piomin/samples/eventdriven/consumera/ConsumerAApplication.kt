package pl.piomin.samples.eventdriven.consumera

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.schema.registry.client.EnableSchemaRegistryClient
import org.springframework.context.annotation.Bean
import pl.piomin.samples.eventdriven.consumera.message.CallmeEvent
import java.util.function.Consumer

@SpringBootApplication
@EnableSchemaRegistryClient
class ConsumerAApplication {

    val logger: Logger = LoggerFactory.getLogger(ConsumerAApplication::class.java)

    @Bean
    fun callmeEventConsumer(): Consumer<CallmeEvent> = Consumer {
        logger.info("Received: {}", it)
    }
}

fun main(args: Array<String>) {
    runApplication<ConsumerAApplication>(*args)
}