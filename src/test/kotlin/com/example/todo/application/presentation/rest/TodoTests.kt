package com.example.todo.application.presentation.rest


import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TodoTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `findAll`() {
        for (i in 1..3) {
            mockMvc
                .post("/api/todo/create/") { content = "foo$i" }
                .andExpect { status { isOk() } }
        }
        mockMvc.put("/api/todo/1/complete/").andExpect { status { isOk() } }

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "false")

        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.content").value("foo2") }
            .andExpect { jsonPath("\$.content").value("foo3") }
            .andExpect { jsonPath("\$.totalElements").value("2") }

        queryParams.clear()
        queryParams.add("isCompleted", "true")

        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.content").value("foo1") }
            .andExpect { jsonPath("\$.totalElements").value("1") }
    }

    @Test
    fun `create`() {
        mockMvc
            .post("/api/todo/create/") { content = "foo" }
            .andExpect { status { isOk() } }

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "false")

        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.content").value("foo") }
            .andExpect { jsonPath("\$.totalElements").value("1") }
    }

    @Test
    fun `complete`() {
        mockMvc
            .post("/api/todo/create/") { content = "foo" }
            .andExpect { status { isOk() } }

        mockMvc
            .put("/api/todo/1/complete/")
            .andExpect { status { isOk() } }

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "true")
        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.totalElements").value("1") }
    }

    @Test
    fun `delete`() {
        mockMvc
            .post("/api/todo/create/") { content = "foo" }
            .andExpect { status { isOk() } }

        mockMvc
            .delete("/api/todo/1/")
            .andExpect { status { isOk() } }

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "false")
        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.totalElements").value("0") }
    }
}