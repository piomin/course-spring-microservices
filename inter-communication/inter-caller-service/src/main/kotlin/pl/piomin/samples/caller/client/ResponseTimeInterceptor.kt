package pl.piomin.samples.caller.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class ResponseTimeInterceptor(private val responseTimeHistory: ResponseTimeHistory) : ClientHttpRequestInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(ResponseTimeInterceptor::class.java)

    override fun intercept(request: HttpRequest, array: ByteArray,
                           execution: ClientHttpRequestExecution): ClientHttpResponse {
        val startTime: Long = System.currentTimeMillis()
        val response: ClientHttpResponse = execution.execute(request, array) // 1
        val endTime: Long = System.currentTimeMillis()
        val responseTime: Long = endTime - startTime
        logger.info("Response time: instance->{}, time->{}", MDC.get("address"), responseTime)
        responseTimeHistory.addNewMeasure(MDC.get("address"), responseTime) // 2
        return response
    }
}