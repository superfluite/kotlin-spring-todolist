package com.example.todo.domain.todo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

@DataJpaTest
class RepositoryTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val todoRepository: TodoRepository,
) {
    @Test
    fun `findAllByIsCompletedOrderByCreatedAtDesc`() {
        val completedTodo = Todo(content = "todo1", isCompleted = true, createdAt = LocalDateTime.of(2021, 11, 1, 0, 0))
        val completedTodo2 = Todo(content = "todo2", isCompleted = true, createdAt = LocalDateTime.of(2021, 11, 2, 0, 0))
        val inCompletedTodo = Todo(content = "todo3")
        entityManager.persist(completedTodo)
        entityManager.persist(completedTodo2)
        entityManager.persist(inCompletedTodo)
        entityManager.flush()

        val actual = todoRepository.findAllByIsCompletedOrderByCreatedAtDesc(Pageable.ofSize(1), true)
        assert(actual.contains(completedTodo2))
        assert(actual.size == 1)

        val actual2 = todoRepository.findAllByIsCompletedOrderByCreatedAtDesc(Pageable.ofSize(1), false)
        assert(actual2.size == 1)
        assert(actual2.contains(inCompletedTodo))
    }
}