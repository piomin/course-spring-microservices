package pl.piomin.samples.caller.client

import org.springframework.cloud.client.ServiceInstance
import java.net.URI

class StaticServiceInstance(private val serviceName: String,
                            private val address: String) : ServiceInstance {

    override fun getServiceId(): String = serviceName

    override fun getMetadata(): MutableMap<String, String> = mutableMapOf()

    override fun getPort(): Int = address.split(":", ignoreCase = false, limit = 2).last().toInt()

    override fun getHost(): String = address.split(":", ignoreCase = false, limit = 2).first().trim()

    override fun getUri(): URI {
        TODO("not implemented")
    }

    override fun isSecure(): Boolean = false
}