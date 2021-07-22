package pl.piomin.samples.eventdriven.schema

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.schema.registry.EnableSchemaRegistryServer

@SpringBootApplication
@EnableSchemaRegistryServer
class SchemaServerApplication

fun main(args: Array<String>) {
    runApplication<SchemaServerApplication>(*args)
}