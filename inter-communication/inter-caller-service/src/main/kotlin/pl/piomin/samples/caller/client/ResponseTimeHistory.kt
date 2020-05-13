package pl.piomin.samples.caller.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.random.Random

class ResponseTimeHistory(private val history: MutableMap<String, Queue<Long>> = mutableMapOf(),
                          val stats: MutableMap<String, Long> = mutableMapOf()) {

    private val logger: Logger = LoggerFactory.getLogger(ResponseTimeHistory::class.java)

    fun addNewMeasure(address: String, measure: Long) {
        var list: Queue<Long>? = history[address]
        if (list == null) {
            history[address] = LinkedList<Long>()
            list = history[address]
        }
        logger.info("Adding new measure for->{}, measure->{}", address, measure)
        if (measure == 0L)
            list!!.add(1L)
        else list!!.add(measure)
        if (list.size > 9)
            list.remove()
        stats[address] = countAvg(address)
        logger.info("Counting avg for->{}, stat->{}", address, stats[address])
    }

    private fun countAvg(address: String): Long {
        val list: Queue<Long>? = history[address]
        return list?.sum()?.div(list.size) ?: 0
    }

    fun getAddress(numberOfInstances: Int): String? {
        if (stats.size < numberOfInstances)
            return null
        var sum: Long = 0
        stats.forEach { sum += it.value }
        var r: Long = Random.nextLong(100)
        var current: Long = 0
        stats.forEach {
            val weight: Long = (sum - it.value)*100 / sum
            logger.info("Weight for->{}, value->{}, random->{}", it.key, weight, r)
            current += weight
            if (r <= current)
                return it.key
        }
        return null
    }

}