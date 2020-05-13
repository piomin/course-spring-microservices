package pl.piomin.samples.caller.client

import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.core.env.Environment
import pl.piomin.samples.caller.config.LoadBalancerConfigurationProperties
import reactor.core.publisher.Flux

class StaticServiceInstanceListSupplier(private val properties: LoadBalancerConfigurationProperties,
                                        private val environment: Environment) : ServiceInstanceListSupplier {

    override fun getServiceId(): String = environment.getProperty("loadbalancer.client.name")!!

    override fun get(): Flux<MutableList<ServiceInstance>> {
        val serviceConfig: LoadBalancerConfigurationProperties.ServiceConfig? =
                properties.instances.find { it.name == serviceId }
        val list: MutableList<ServiceInstance> =
                serviceConfig!!.servers.split(",", ignoreCase = false, limit = 0)
                        .map { StaticServiceInstance(serviceId, it) }.toMutableList()
        return Flux.just(list)
    }

}