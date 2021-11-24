package com.example.todo.domain.todo

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

interface TodoService {
    fun findAll(isCompleted: Boolean, pageable: Pageable) : Iterable<Todo>

    fun create(content: String) : Todo

    fun delete(id: Long)

    fun complete(id: Long)
}

@Service
class SimpleTodoService(private val repository: TodoRepository) : TodoService {
    override fun findAll(isCompleted: Boolean, pageable: Pageable) : Iterable<Todo> {
        return repository.findAllByIsCompletedOrderByCreatedAtDesc(pageable, isCompleted)
    }

    override fun create(content: String) : Todo {
        val entity = Todo(content = content)
        repository.save(entity)
        return entity
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    override fun complete(id: Long) {
        val todo = repository.getById(id)
        todo.isCompleted = true
        repository.save(todo)
    }
}