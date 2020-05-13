package pl.piomin.samples.caller.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("spring.cloud.loadbalancer")
class LoadBalancerConfigurationProperties {

    val instances: MutableList<ServiceConfig> = mutableListOf()

    class ServiceConfig {
        var name: String = ""
        var servers: String = ""
    }

}