package pl.piomin.samples.callme.model

import java.time.LocalDateTime

data class Conversation(val time: LocalDateTime = LocalDateTime.now(),
                        val request: CallmeRequest,
                        val response: CallmeResponse)