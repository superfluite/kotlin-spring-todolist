import org.springframework.data.repository.CrudRepository
import java.util.*

interface TodoRepository : CrudRepository<Todo, UUID> {
    fun findAllTodo() {
        // TODO
    }

    fun create() {
        // TODO
    }

    fun delete() {
        // TODO
    }

    fun complete() {
        // TODO
    }
}
