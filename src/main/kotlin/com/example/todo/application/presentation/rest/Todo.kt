import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/todo")
class TodoController(private val repository: TodoRepository) {
    @GetMapping("/")
    fun findAll() {
        // TODO
    }

    @GetMapping("/create/")
    fun create(@RequestParam(value="content") content: String) {
        // TODO
    }

    @GetMapping("/complete/{id}/")
    fun complete(@PathVariable id: UUID) {
        // TODO
    }

    @GetMapping("/delete/{id}/")
    fun delete(@PathVariable id: UUID) {
        // TODO
    }
}
