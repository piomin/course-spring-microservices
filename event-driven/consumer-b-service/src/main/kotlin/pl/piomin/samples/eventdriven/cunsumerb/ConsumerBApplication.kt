package pl.piomin.samples.eventdriven.cunsumerb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConsumerBApplication

fun main(args: Array<String>) {
    runApplication<ConsumerBApplication>(*args)
}