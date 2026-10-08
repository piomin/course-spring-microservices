package pl.piomin.samples.callme.repository

import org.springframework.stereotype.Repository
import pl.piomin.samples.callme.model.Conversation
import java.util.concurrent.CopyOnWriteArrayList

@Repository
class ConversationRepository {

    private val conversations: MutableList<Conversation> = CopyOnWriteArrayList()

    fun findByRequestId(requestId: Int): Conversation? = conversations.find { it.request.id == requestId }

    fun findAll(): List<Conversation> = conversations.toList()

    fun add(conversation: Conversation) {
        conversations.add(conversation)
    }

}
