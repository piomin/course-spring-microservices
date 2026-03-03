package pl.piomin.samples.intro.repository

import org.springframework.stereotype.Repository
import pl.piomin.samples.intro.domain.Person
import java.util.concurrent.CopyOnWriteArrayList

@Repository
class PersonRepository {

    private val persons: MutableList<Person> = CopyOnWriteArrayList()

    fun findById(id: Int): Person? = persons.find { it.id == id }

    fun findAll(): List<Person> = persons.toList()

    fun delete(id: Int) {
        persons.removeIf { it.id == id }
    }

    fun add(person: Person): Person {
        persons.add(person)
        return person
    }

    fun update(person: Person): Person {
        val index = persons.indexOfFirst { it.id == person.id }
        if (index == -1) throw NoSuchElementException("Person with id ${person.id} not found")
        persons[index] = person
        return person
    }
}
