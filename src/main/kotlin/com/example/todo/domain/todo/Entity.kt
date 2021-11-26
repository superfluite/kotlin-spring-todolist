package com.example.todo.domain.todo

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="todo")
class Todo(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var content: String,
    var isCompleted: Boolean = false,
)