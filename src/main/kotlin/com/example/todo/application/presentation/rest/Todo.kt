package com.example.todo.application.presentation.rest

import com.example.todo.domain.todo.SimpleTodoService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/todo")
class TodoController(private val service: SimpleTodoService) {
    @GetMapping("/")
    fun findAll(@RequestParam(value="isCompleted") isCompleted: Boolean, pageable: Pageable) = service.findAll(isCompleted, pageable)

    @PostMapping("/create/")
    fun create(@RequestBody content: String) = service.create(content)

    @PutMapping("/{id}/complete/")
    fun complete(@PathVariable id: Long) = service.complete(id)

    @DeleteMapping("/{id}/")
    fun delete(@PathVariable id: Long) = service.delete(id)
}
