package pl.piomin.samples.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class DiscoveryServerApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryServerApplication>(*args)
}
