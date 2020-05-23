package pl.piomin.samples.eventdriven.consumera

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConsumerAApplication

fun main(args: Array<String>) {
    runApplication<ConsumerAApplication>(*args)
}