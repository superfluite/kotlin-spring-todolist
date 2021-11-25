package com.example.todo.domain.todo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface TodoService {
    fun findAll(isCompleted: Boolean, pageable: Pageable) : Iterable<Todo>

    fun create(content: String) : Todo

    fun delete(id: Long)

    fun complete(id: Long)
}

@Service
@Transactional
class SimpleTodoService(private val repository: TodoRepository) : TodoService {
    override fun findAll(isCompleted: Boolean, pageable: Pageable) : Page<Todo> {
        return repository.findAllByIsCompletedOrderByCreatedAtDesc(pageable, isCompleted)
    }

    override fun create(content: String) : Todo {
        return repository.save(Todo(content = content))
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    override fun complete(id: Long) {
        val todo = repository.getById(id)
        todo.isCompleted = true
    }
}