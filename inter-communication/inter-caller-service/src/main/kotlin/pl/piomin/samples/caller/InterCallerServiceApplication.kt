package pl.piomin.samples.caller

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.netty.channel.ChannelOption
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import pl.piomin.samples.caller.client.CallmeService
import pl.piomin.samples.caller.client.CustomCallmeClientLoadBalancerConfiguration
import reactor.netty.http.client.HttpClient
import java.time.Duration

@SpringBootApplication
@EnableFeignClients
@LoadBalancerClient(value = "inter-callme-service", configuration = [CustomCallmeClientLoadBalancerConfiguration::class])
class InterCallerServiceApplication {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    @LoadBalanced
    fun restClientBuilder(): RestClient.Builder = RestClient.builder()

    @Bean
    @LoadBalanced
    fun clientBuilder(): WebClient.Builder {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)
            .responseTimeout(Duration.ofMillis(100))
        return WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
    }

    @Bean
    fun callmeService(restClientBuilder: RestClient.Builder): CallmeService {
        val client = restClientBuilder.baseUrl("http://inter-callme-service/callme").build()
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client)).build()
            .createClient(CallmeService::class.java)
    }

    @Bean
    fun defaultCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
        return Customizer { factory: Resilience4JCircuitBreakerFactory ->
            factory.configureDefault { id: String ->
                Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofMillis(500))
                        .build())
                    .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10)
                        .failureRateThreshold(66.6F)
                        .slowCallRateThreshold(66.6F)
                        .build())
                    .build()
            }
        }
    }

}

fun main(args: Array<String>) {
    runApplication<InterCallerServiceApplication>(*args)
}
