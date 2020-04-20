package pl.piomin.samples.callme.repository

import org.springframework.stereotype.Repository
import pl.piomin.samples.callme.model.Conversation

@Repository
class ConversationRepository(private val conversations: MutableList<Conversation> = mutableListOf()) {

    fun findByRequestId(requestId: Int): Conversation? = conversations.find { it.request.id == requestId }

    fun findAll(): MutableList<Conversation> = conversations

    fun add(conversation: Conversation) = conversations.add(conversation)

}