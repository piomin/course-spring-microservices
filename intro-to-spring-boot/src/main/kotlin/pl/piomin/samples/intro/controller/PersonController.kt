package pl.piomin.samples.intro.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.piomin.samples.intro.domain.Person
import pl.piomin.samples.intro.repository.PersonRepository

@RestController
@RequestMapping("/persons")
class PersonController(private val repository: PersonRepository) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<Person> =
        repository.findById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping
    fun findAll(): List<Person> = repository.findAll()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        repository.delete(id)
    }

    @PutMapping
    fun update(@RequestBody person: Person): Person = repository.update(person)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody person: Person): Person = repository.add(person)

}
