package pl.piomin.samples.caller.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.ObjectProvider
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse
import org.springframework.cloud.client.loadbalancer.reactive.EmptyResponse
import org.springframework.cloud.client.loadbalancer.reactive.Request
import org.springframework.cloud.client.loadbalancer.reactive.Response
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicInteger

class WeightedTimeResponseLoadBalancer(
        private val serviceInstanceListSupplierProvider: ObjectProvider<ServiceInstanceListSupplier>,
        private val serviceId: String,
        private val responseTimeHistory: ResponseTimeHistory) : ReactorServiceInstanceLoadBalancer {

    private val logger: Logger = LoggerFactory.getLogger(WeightedTimeResponseLoadBalancer::class.java)
    private val position: AtomicInteger = AtomicInteger()

    override fun choose(request: Request<*>?): Mono<Response<ServiceInstance>> {
        val supplier: ServiceInstanceListSupplier = serviceInstanceListSupplierProvider
                .getIfAvailable { NoopServiceInstanceListSupplier() }
        return supplier.get().next()
                .map { serviceInstances: List<ServiceInstance> -> getInstanceResponse(serviceInstances) }
    }

    private fun getInstanceResponse(instances: List<ServiceInstance>): Response<ServiceInstance> {
        return if (instances.isEmpty()) {
            EmptyResponse()
        } else {
            val address: String? = responseTimeHistory.getAddress(instances.size)
            val pos: Int = position.incrementAndGet()
            var instance: ServiceInstance = instances[pos % instances.size]
            if (address != null) {
                val found: ServiceInstance? = instances.find { "${it.host}:${it.port}" == address }
                if (found != null)
                    instance = found
            }
            logger.info("Current instance: [address->{}:{}, stats->{}ms]", instance.host, instance.port,
                    responseTimeHistory.stats["${instance.host}:${instance.port}"])
            MDC.put("address", "${instance.host}:${instance.port}")
            DefaultResponse(instance)
        }
    }
}