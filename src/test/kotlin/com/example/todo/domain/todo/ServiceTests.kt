package com.example.todo.domain.todo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class ServiceTests @Autowired constructor(
    val todoService: SimpleTodoService,
) {

    @Test
    fun `findAll`() {
        val completedTodo = todoService.create("todo1")
        val completedTodo2 = todoService.create("todo2")
        val inCompletedTodo = todoService.create("todo3")
        todoService.complete(completedTodo.id)
        todoService.complete(completedTodo2.id)

        val actual = todoService.findAll(true, Pageable.ofSize(1))
        assert(actual.count() == 1)
        assert(actual.contains(completedTodo2))

        val actual2 = todoService.findAll(false, Pageable.ofSize(1))
        assert(actual2.count() == 1)
        assert(actual2.contains(inCompletedTodo))
    }

    @Test
    fun `create`() {
        val actual = todoService.create("todo")
        assert(actual.content == "todo")
        assert(!actual.isCompleted)
        val actual2 = todoService.findAll(false, Pageable.ofSize(1))
        assert(actual2.contains(actual))
    }

    @Test
    fun `delete`() {
        val todo = todoService.create("todo")
        todoService.delete(todo.id)
        val actual = todoService.findAll(false, Pageable.ofSize(10))
        assert(actual.count() == 0)
    }

    @Test
    fun `complete`() {
        val todo = todoService.create("todo")

        assert(!todo.isCompleted)
        todoService.complete(todo.id)
        assert(todo.isCompleted)
    }
}