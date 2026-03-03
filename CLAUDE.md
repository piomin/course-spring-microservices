# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Use all skills from the `.claude/.agents/skills` direcotry.

## Project Overview

Maven multi-module project demonstrating Spring Cloud microservices patterns, used as a course repository with 5 progressive modules.

## Build Commands

```bash
# Build entire project with tests
mvn clean verify

# Build without tests
mvn clean install -DskipTests

# Run a specific service
mvn -pl <module>/<service> spring-boot:run
# Examples:
mvn -pl config-and-discovery/discovery-server spring-boot:run
mvn -pl inter-communication/inter-caller-service spring-boot:run
mvn -pl api-gateway/gateway-service spring-boot:run

# Build Docker image (intro module only, uses Jib)
mvn jib:build -pl intro-to-spring-boot

# Run SonarCloud analysis
mvn verify sonar:sonar
```

## Docker Compose

```bash
# ELK stack for intro-to-spring-boot (Elasticsearch:9200, Logstash:5000, Kibana:5601)
docker-compose -f intro-to-spring-boot/docker-compose.yml up

# Gateway infrastructure (Redis:6379, Eureka discovery:8761)
docker-compose -f api-gateway/docker-compose.yml up
```

## Technology Stack

- **Spring Boot:** 4.0.3 | **Spring Cloud:** 2025.1.1 | **Java:** 21
- **Language:** Kotlin 2.3.10 (primary), some Java
- **Build:** Maven 3.9+, Jib plugin for Docker, Jacoco for coverage

## Module Architecture

| Module | Services | Key Tech |
|--------|----------|----------|
| `intro-to-spring-boot` | simple-service | Spring Web, Prometheus metrics, ELK logging |
| `config-and-discovery` | config-server, discovery-server, caller-service, callme-service | Eureka, Spring Cloud Config |
| `inter-communication` | inter-caller-service, inter-callme-service | OpenFeign, HTTP Service Interface, WebFlux, Resilience4j, OpenTelemetry |
| `api-gateway` | gateway-service | Spring Cloud Gateway (WebFlux), Redis rate limiting, circuit breaker |
| `event-driven` | producer-service, consumer-a-service, consumer-b-service | Spring Cloud Stream, RabbitMQ |

## Key Architectural Patterns

**Service Discovery:** Eureka Server in `config-and-discovery/discovery-server`. Services register by name; clients resolve addresses dynamically.

**Inter-Service Communication (3 approaches in `inter-communication`):**
1. **OpenFeign** — Declarative client via `@FeignClient(name = "inter-callme-service", path = "/callme")`
2. **HTTP Service Interface** — Spring 6.1+ native `@HttpExchange` / `@PostExchange` annotations (see `CallmeService.kt`)
3. **Custom WebFlux client** — Reactive with weighted response-time load balancing (`WeightedTimeResponseLoadBalancer.kt`)

**API Gateway:** Reactive Spring Cloud Gateway with Redis-backed rate limiting, Resilience4j circuit breaker, and path-based routing to downstream services.

**Event-Driven:** Spring Cloud Stream abstraction over RabbitMQ. Producer partitions events by ID; consumers use named groups for independent processing.

**Observability Stack:**
- Metrics: Micrometer → Prometheus
- Tracing: OpenTelemetry → Zipkin
- Logging: Logstash encoder → Elasticsearch → Kibana

## Package Convention

All services follow: `pl.piomin.samples.<module-name>.*`

Example: `pl.piomin.samples.caller.*`, `pl.piomin.samples.callme.*`

## Spring Boot 4 Breaking Changes

Key differences vs Spring Boot 3.x relevant to this codebase:

- **`TestRestTemplate` removed** — Use `MockMvc` built from `WebApplicationContext` via `MockMvcBuilders.webAppContextSetup(context).build()`
- **`@AutoConfigureMockMvc` removed** — The entire `spring-boot-test-autoconfigure.web` slice is gone; wire `WebApplicationContext` manually in tests
- **`springdoc-openapi` 2.x incompatible** — `WebMvcProperties` was removed; use `springdoc-openapi 3.x` (currently `3.0.2`)
- **`git-commit-id-plugin`** (groupId `pl.project13.maven`) deprecated; use `io.github.git-commit-id:git-commit-id-maven-plugin` (managed by Spring Boot BOM)
- **Virtual threads** — Enable with `spring.threads.virtual.enabled: true`; use `CopyOnWriteArrayList` for shared mutable state
- **Spring Cloud 2025.0.x incompatible with Spring Boot 4** — `LifecycleMvcEndpointAutoConfiguration` references removed `org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration`; Spring Cloud 2025.1.1+ (spring-cloud-context 5.x) is required
- **Spring Cloud 2025.1.1 API changes** — `ReactorServiceInstanceLoadBalancer.choose()` signature changed from `Request<*>?` to `Request<Any>`; `configureDefault` lambda `id` parameter is now non-nullable `String`
- **`@MockBean` → `@MockitoBean`** — Use `org.springframework.test.context.bean.override.mockito.MockitoBean`; pair with `mockito-kotlin` for idiomatic Kotlin usage
- **`MockRestServiceServer` with load-balanced `RestTemplate`** — Use `containsString("/path")` matcher since LB resolves service names to `host:port`
- **`@WebFluxTest` removed** — Use `WebTestClient.bindToController(MyController()).build()` for standalone WebFlux controller tests (no Spring context needed, no Redis/Eureka required)
- **Jackson 3.0** — Package renamed from `com.fasterxml.jackson` → `tools.jackson` (e.g. `tools.jackson.databind.ObjectMapper`). Spring Boot 4 ships `tools.jackson.core:jackson-databind:3.0.x`. The `spring-boot-starter-json` starter was renamed to `spring-boot-starter-jackson`
- **Spring Cloud Stream test binder** — `InputDestination.send(msg, name)` and `OutputDestination.receive(timeout, name)` use the **destination name** (e.g. `callme-events`), not the binding name (e.g. `callmeEventConsumer-in-0`)

## CI/CD

- **CircleCI** runs Maven tests and SonarCloud analysis on each push
- **Renovate** manages automated dependency updates (configured in `renovate.json`)