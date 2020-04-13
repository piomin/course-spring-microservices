package pl.piomin.samples.caller.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/caller")
class CallerController {

    @Value("\${property1}")
    lateinit var property1: String
    @Value("\${property2}")
    lateinit var property2: String

    @GetMapping("/property1")
    fun findProperty1(): String = property1

    @GetMapping("/property2")
    fun findProperty2(): String = property2

}