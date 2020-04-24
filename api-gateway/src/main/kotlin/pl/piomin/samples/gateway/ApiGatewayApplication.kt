package pl.piomin.samples.gateway

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Mono
import java.time.Duration

@SpringBootApplication
class ApiGatewayApplication {

	@Bean
	fun keyResolver(): KeyResolver = KeyResolver { _ -> Mono.just("1") }

	@Bean
	fun defaultCustomizer(): Customizer<ReactiveResilience4JCircuitBreakerFactory> {
		return Customizer { factory: ReactiveResilience4JCircuitBreakerFactory ->
			factory.configureDefault { id: String? ->
				Resilience4JConfigBuilder(id)
						.timeLimiterConfig(TimeLimiterConfig.custom()
								.timeoutDuration(Duration.ofMillis(500))
								.build())
						.circuitBreakerConfig(CircuitBreakerConfig.custom()
								.slidingWindowSize(10)
								.failureRateThreshold(33.3F)
								.slowCallRateThreshold(33.3F)
								.build())
						.build()
			}
		}
	}

}

fun main(args: Array<String>) {
	runApplication<ApiGatewayApplication>(*args)
}
