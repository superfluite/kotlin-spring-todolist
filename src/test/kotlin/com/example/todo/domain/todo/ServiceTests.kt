package com.example.todo.domain.todo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

@DataJpaTest
class ServiceTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val todoRepository: TodoRepository,
) {
    @Test
    fun `findAll`() {
        val todoService = SimpleTodoService(todoRepository)
        val completedTodo = Todo(content = "todo1", isCompleted = true, createdAt = LocalDateTime.of(2021, 11, 1, 0, 0))
        val completedTodo2 = Todo(content = "todo2", isCompleted = true, createdAt = LocalDateTime.of(2021, 11, 2, 0, 0))
        val inCompletedTodo = Todo(content = "todo3")
        entityManager.persist(completedTodo)
        entityManager.persist(completedTodo2)
        entityManager.persist(inCompletedTodo)
        entityManager.flush()

        val actual = todoService.findAll(true, Pageable.ofSize(1))
        assert(actual.count() == 1)
        assert(actual.contains(completedTodo2))

        val actual2 = todoService.findAll(false, Pageable.ofSize(1))
        assert(actual2.count() == 1)
        assert(actual2.contains(inCompletedTodo))
    }

    @Test
    fun `create`() {
        val todoService = SimpleTodoService(todoRepository)
        val actual = todoService.create(content = "todo")
        assert(actual.content == "todo")
        assert(!actual.isCompleted)
    }

    @Test
    fun `delete`() {
        val todoService = SimpleTodoService(todoRepository)
        val todo = Todo(content = "todo")
        entityManager.persist(todo)
        entityManager.flush()

        todoService.delete(todo.id)
        val actual = todoService.findAll(false, Pageable.ofSize(10))
        assert(actual.count() == 0)
    }

    @Test
    fun `complete`() {
        val todoService = SimpleTodoService(todoRepository)
        val todo = Todo(content = "todo")
        entityManager.persist(todo)
        entityManager.flush()

        assert(!todo.isCompleted)
        todoService.complete(todo.id)
        assert(todo.isCompleted)
    }
}