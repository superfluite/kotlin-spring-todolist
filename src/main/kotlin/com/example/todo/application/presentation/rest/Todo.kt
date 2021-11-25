package com.example.todo.application.presentation.rest

import com.example.todo.domain.todo.SimpleTodoService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api/todo")
class TodoController(private val service: SimpleTodoService) {
    @GetMapping("/")
    fun findAll(@RequestParam(value="isCompleted") isCompleted: Boolean, pageable: Pageable) = service.findAll(isCompleted, pageable)

    @PostMapping("/create/")
    fun create(@RequestBody content: String) = service.create(content)

    @PutMapping("/complete/{id}/")
    fun complete(@PathVariable id: Long) = service.complete(id)

    @DeleteMapping("/delete/{id}/")
    fun delete(@PathVariable id: Long) = service.delete(id)
}
