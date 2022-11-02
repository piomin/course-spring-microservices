package pl.piomin.samples.caller.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager
import org.springframework.cloud.loadbalancer.config.LoadBalancerZoneConfig
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import pl.piomin.samples.caller.config.LoadBalancerConfigurationProperties


class CustomCallmeClientLoadBalancerConfiguration {

    @Autowired
    lateinit var properties: LoadBalancerConfigurationProperties

    @Bean
    fun discoveryClientServiceInstanceListSupplier(discoveryClient: ReactiveDiscoveryClient, environment: Environment,
                                                   zoneConfig: LoadBalancerZoneConfig,
                                                   context: ApplicationContext): ServiceInstanceListSupplier {
//        val firstDelegate = DiscoveryClientServiceInstanceListSupplier(discoveryClient, environment)
//        val delegate = ZonePreferenceServiceInstanceListSupplier(firstDelegate,
//                zoneConfig)
        val delegate = StaticServiceInstanceListSupplier(properties, environment)
        val cacheManagerProvider = context.getBeanProvider(LoadBalancerCacheManager::class.java)
        return if (cacheManagerProvider.ifAvailable != null) {
            CachingServiceInstanceListSupplier(delegate, cacheManagerProvider.ifAvailable)
        } else delegate
    }

    @Bean
    fun loadBalancer(environment: Environment, loadBalancerClientFactory: LoadBalancerClientFactory):
            ReactorLoadBalancer<ServiceInstance> {
        val name: String? = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME)
//        return WeightedTimeResponseLoadBalancer(
//                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier::class.java),
//                name!!, responseTimeHistory)
        return RandomLoadBalancer(
            loadBalancerClientFactory
                .getLazyProvider(name, ServiceInstanceListSupplier::class.java),
            name!!
        )
    }
}