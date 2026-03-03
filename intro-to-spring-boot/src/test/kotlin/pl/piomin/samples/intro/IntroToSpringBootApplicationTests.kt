package pl.piomin.samples.intro

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IntroToSpringBootApplicationTests {

    @Autowired
    lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    @Test
    @Order(1)
    fun testAdd() {
        mockMvc.post("/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"id":1,"firstName":"John","lastName":"Smith","age":30}"""
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { value(1) }
            jsonPath("$.firstName") { value("John") }
        }
    }

    @Test
    @Order(2)
    fun testFindById() {
        mockMvc.get("/persons/{id}", 1).andExpect {
            status { isOk() }
            jsonPath("$.id") { value(1) }
            jsonPath("$.age") { value(30) }
        }
    }

    @Test
    @Order(3)
    fun testFindByIdNotFound() {
        mockMvc.get("/persons/{id}", 999).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @Order(4)
    fun testFindAll() {
        mockMvc.get("/persons").andExpect {
            status { isOk() }
            jsonPath("$", hasSize<Int>(1))
        }
    }

    @Test
    @Order(5)
    fun testPut() {
        mockMvc.put("/persons") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"id":1,"firstName":"John","lastName":"Smith","age":31}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.age") { value(31) }
        }
    }

    @Test
    @Order(6)
    fun testDelete() {
        mockMvc.delete("/persons/{id}", 1).andExpect {
            status { isNoContent() }
        }
    }

}
