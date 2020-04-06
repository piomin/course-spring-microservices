package pl.piomin.samples.intro.controller

import org.springframework.web.bind.annotation.*
import pl.piomin.samples.intro.domain.Person
import pl.piomin.samples.intro.repository.PersonRepository

@RestController
@RequestMapping("/persons")
class PersonController(private val repository: PersonRepository) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): Person? = repository.findById(id)

    @GetMapping
    fun findAll(): MutableList<Person> = repository.findAll()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int) = repository.delete(id)

    @PutMapping
    fun update(@RequestBody person: Person) = repository.update(person)

    @PostMapping
    fun add(@RequestBody person: Person) = repository.add(person)

}