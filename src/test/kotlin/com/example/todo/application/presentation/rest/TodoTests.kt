package com.example.todo.application.presentation.rest


import com.example.todo.domain.todo.Todo
import com.example.todo.domain.todo.TodoRepository
import org.json.JSONObject
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

    private fun createTodo(todoContent : String) : Any {
        mockMvc
            .post("/api/todo/create/") { content = todoContent }

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "false")

        val actual = mockMvc
            .get("/api/todo/") { params = queryParams}
            .andReturn()

        return (JSONObject(actual.response.contentAsString)
            .getJSONArray("content")[0] as JSONObject)
            .get("id")
    }

    @Test
    fun `findAll`() {
        val todoIds = mutableListOf<Int>()
        for (i in 1..3) {
            createTodo("foo$i")
            todoIds.add(i)
        }
        mockMvc.get("/api/todo/${todoIds[0]}/complete/")

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
        val todoId = createTodo("foo")

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "false")

        mockMvc
            .put("/api/todo/$todoId/complete/")
            .andExpect { status { isOk() } }

        queryParams.clear()
        queryParams.add("isCompleted", "true")
        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.totalElements").value("1") }
    }

    @Test
    fun `delete`() {
        val todoId = createTodo("foo")

        mockMvc
            .delete("/api/todo/$todoId/")
            .andExpect { status { isOk() } }

        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.add("isCompleted", "false")
        mockMvc
            .get("/api/todo/") { params = queryParams }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("\$.totalElements").value("0") }
    }
}