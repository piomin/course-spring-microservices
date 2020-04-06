package pl.piomin.samples.intro.repository

import org.springframework.stereotype.Repository
import pl.piomin.samples.intro.domain.Person

@Repository
class PersonRepository(private val persons: MutableList<Person> = mutableListOf()) {

    fun findById(id: Int): Person? = persons.find { it.id == id }

    fun findAll(): MutableList<Person> = persons

    fun delete(id: Int) = persons.removeIf { it.id == id }

    fun add(person: Person) = persons.add(person)

    fun update(person: Person) {
        val index: Int = persons.indexOfFirst { it.id == person.id }
        persons[index] = person
    }
}