import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name="todo")
class Todo(
    @Id @GeneratedValue var id: UUID,
    var createdAt: LocalDateTime,
    var content: String,
    var isCompleted: Boolean = false,
)