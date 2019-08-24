package ml.wonwoo.todoweb.todo

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/todo")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun findAll(): List<TodoDto> = todoService.findAll().map { it.dto() }

    @PostMapping
    fun save(todoRequest: TodoRequest) = todoService.save(todoRequest.toTodo()).dto()

    @PutMapping("/{id}")
    fun completed(@PathVariable id: Long, todoCompleted : TodoCompleted) = todoService.completed(id ,todoCompleted.completed)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = todoService.delete(id)

}


fun TodoRequest.toTodo() = Todo(title = this.title, completed = false)

data class TodoCompleted(

    val completed: Boolean

)


data class TodoRequest(

    val title: String
)

data class TodoDto(

    val id: Long?,

    val title: String,

    val completed: Boolean,

    val regDate: LocalDateTime
)

fun Todo.dto() = TodoDto(id = this.id, title = this.title, completed = this.completed, regDate = this.regDate)