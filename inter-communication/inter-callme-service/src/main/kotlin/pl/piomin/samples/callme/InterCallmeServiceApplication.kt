package pl.piomin.samples.callme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CallmeServiceApplication

fun main(args: Array<String>) {
    runApplication<CallmeServiceApplication>(*args)
}