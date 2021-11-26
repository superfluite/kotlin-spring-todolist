package com.example.todo.application.presentation.rest

import com.example.todo.domain.todo.SimpleTodoService
import com.example.todo.domain.todo.Todo
import com.example.todo.domain.todo.TodoDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/todo")
class TodoController(private val service: SimpleTodoService) {
    @GetMapping("/")
    fun findAll(@RequestParam(value="isCompleted") isCompleted: Boolean, pageable: Pageable) : Page<TodoDTO> {
        val todoList : Page<Todo> = service.findAll(isCompleted, pageable)
        return todoList.map {
            todo -> TodoDTO(
                id = todo.id,
                createdAt = todo.createdAt,
                content = todo.content,
                isCompleted = todo.isCompleted,
            )
        }
    }

    @PostMapping("/create/")
    fun create(@RequestBody content: String) = service.create(content)

    @PutMapping("/{id}/complete/")
    fun complete(@PathVariable id: Long) = service.complete(id)

    @DeleteMapping("/{id}/")
    fun delete(@PathVariable id: Long) = service.delete(id)
}
