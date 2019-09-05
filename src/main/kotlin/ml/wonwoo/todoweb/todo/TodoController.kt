package ml.wonwoo.todoweb.todo

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/todo")
class TodoController(private val todoService: TodoService, private val simpMessagingTemplate: SimpMessagingTemplate) {

    @GetMapping
    fun findAll(): List<TodoDto> = todoService.findAll().map { it.dto() }

    @PostMapping
    fun save(@RequestBody todoRequest: TodoRequest): TodoDto {

        val todoDto = todoService.save(todoRequest.toTodo()).dto()

        simpMessagingTemplate.convertAndSend("/todo/message", todoDto)

        return todoDto

    }

    @PutMapping("/{id}")
    fun completed(@PathVariable id: Long, @RequestBody todoCompleted: TodoCompleted) = todoService.completed(id, todoCompleted.completed)

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