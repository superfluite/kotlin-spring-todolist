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
    val todoRepository: TodoRepository,
) {

    @Test
    fun `findAll`() {
        val completedTodo = Todo(content = "todo1", isCompleted = true)
        val completedTodo2 = Todo(content = "todo2", isCompleted = true)
        val inCompletedTodo = Todo(content = "todo3")
        todoRepository.save(completedTodo)
        todoRepository.save(completedTodo2)
        todoRepository.save(inCompletedTodo)

        var actual = todoService.findAll(true, Pageable.ofSize(1))
        assert(actual.count() == 1)
        assert(actual.contains(completedTodo2))

        actual = todoService.findAll(false, Pageable.ofSize(1))
        assert(actual.count() == 1)
        assert(actual.contains(inCompletedTodo))
    }

    @Test
    fun `create`() {
        var actual = todoRepository.findAllByIsCompletedOrderByCreatedAtDesc(Pageable.ofSize(10), false)
        assert(actual.content.size == 0)

        todoService.create("todo")
        actual = todoRepository.findAllByIsCompletedOrderByCreatedAtDesc(Pageable.ofSize(10), false)
        assert(actual.content.size == 1)
        assert(actual.content[0].content == "todo")
        assert(!actual.content[0].isCompleted)
    }

    @Test
    fun `delete`() {
        val todo = Todo(content = "todo")
        todoRepository.save(todo)

        todoService.delete(todo.id)
        var actual = todoRepository.findAllByIsCompletedOrderByCreatedAtDesc(Pageable.ofSize(10), false)
        assert(actual.content.size == 0)
    }

    @Test
    fun `complete`() {
        val todo = Todo(content = "todo")
        todoRepository.save(todo)

        todoService.complete(todo.id)
        assert(todo.isCompleted)
    }
}