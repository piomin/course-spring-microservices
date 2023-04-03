package pl.piomin.samples.caller;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflyConfig;
import io.specto.hoverfly.junit5.api.HoverflyCore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.piomin.samples.caller.model.CallmeResponse;

import io.specto.hoverfly.junit.core.SimulationSource.dsl;
import io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@HoverflyCore(config = HoverflyConfig(proxyLocalHost = true, webServer = true, proxyPort = 55800))
@ExtendWith(HoverflyExtension::class)
class CallerControllerTests {

    @Autowired
    lateinit var template: TestRestTemplate

    @Test
    fun ping(hoverfly: Hoverfly) {
        hoverfly.simulate(
                dsl(service("http://localhost:55000")
                        .post("/callme/call")
                        .anyBody()
                        .willReturn(success().body("""{"id":1,"message":"FIRST"}""").header("Content-Type", "application/json")))
        );

        val res = template.postForObject("/caller/send/{message}", null, CallmeResponse::class.java, "Hello");
        assertNotNull(res);
        assertNotNull(res.id);
        assertNotNull(res.message);
        assertEquals("FIRST", res.message);
    }

    @Test
    fun pingFeign(hoverfly: Hoverfly) {
        hoverfly.simulate(
            dsl(service("http://localhost:55000")
                .post("/callme/call")
                .anyBody()
                .willReturn(success().body("""{"id":1,"message":"FIRST"}""").header("Content-Type", "application/json")))
        );

        val res = template.postForObject("/caller-with-feign/send/{message}", null, CallmeResponse::class.java, "Hello");
        assertNotNull(res);
        assertNotNull(res.id);
        assertNotNull(res.message);
        assertEquals("FIRST", res.message);
    }
}