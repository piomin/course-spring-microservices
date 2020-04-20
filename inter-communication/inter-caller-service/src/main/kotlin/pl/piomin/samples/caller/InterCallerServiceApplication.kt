package pl.piomin.samples.caller

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import java.time.Duration
import java.util.concurrent.TimeUnit


@SpringBootApplication
@EnableFeignClients
class InterCallerServiceApplication {

    @Bean
    @LoadBalanced
    fun template(): RestTemplate = RestTemplateBuilder()
//            .setReadTimeout(Duration.ofMillis(100))
//            .setConnectTimeout(Duration.ofMillis(100))
            .build()

    @Bean
    @LoadBalanced
    fun clientBuilder(): WebClient.Builder {
        val tcpClient: TcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)
                .doOnConnected { conn ->
                    conn.addHandlerLast(ReadTimeoutHandler(100, TimeUnit.MILLISECONDS))
                }
        val connector = ReactorClientHttpConnector(HttpClient.from(tcpClient))
        return WebClient.builder().clientConnector(connector)
    }

    @Bean
    fun defaultCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
        return Customizer { factory: Resilience4JCircuitBreakerFactory ->
            factory.configureDefault { id: String? ->
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