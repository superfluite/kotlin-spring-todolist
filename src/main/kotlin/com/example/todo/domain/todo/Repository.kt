package com.example.todo.domain.todo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository


interface TodoRepository : JpaRepository<Todo, Long> {
    fun findAllByIsCompletedOrderByCreatedAtDesc(pageable: Pageable, isCompleted: Boolean): Page<Todo>
}
