package pl.piomin.samples.caller.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

class ResponseTimeInterceptor(private val history: ResponseTimeHistory) : ClientHttpRequestInterceptor {

    private val logger: Logger = LoggerFactory.getLogger(ResponseTimeInterceptor::class.java)

    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        val start = System.currentTimeMillis()
        val response = execution.execute(request, body)
        val end = System.currentTimeMillis()
        val executionTime = end - start
        val address = request.uri.host + ":" + request.uri.port
        history.addNewMeasure(address, executionTime)
        logger.info("Request to {} took {} ms", address, executionTime)
        return response
    }
}