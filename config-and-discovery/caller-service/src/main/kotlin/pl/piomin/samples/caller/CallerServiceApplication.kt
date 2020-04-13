package pl.piomin.samples.caller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CallerServiceApplication

fun main(args: Array<String>) {
    runApplication<CallerServiceApplication>(*args)
}