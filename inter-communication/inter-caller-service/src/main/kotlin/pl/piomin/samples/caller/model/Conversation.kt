package pl.piomin.samples.caller.model

import java.time.LocalDateTime

data class Conversation(val time: LocalDateTime = LocalDateTime.now(),
                        val request: CallmeRequest? = null,
                        val response: CallmeResponse? = null)