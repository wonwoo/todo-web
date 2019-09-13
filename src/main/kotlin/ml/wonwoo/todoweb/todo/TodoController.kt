package ml.wonwoo.todoweb.todo

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@RestController
@RequestMapping("/todo")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun findAll(): Flux<TodoDto> = todoService.findAll().map { it.dto() }

    @PostMapping
    fun save(@RequestBody todoRequest: TodoRequest): Mono<TodoDto> =
        todoService.save(todoRequest.toTodo()).map { it.dto() }

    @PutMapping("/{id}")
    fun completed(@PathVariable id: String, @RequestBody todoCompleted: TodoCompleted): Mono<TodoDto> =
        todoService.completed(id, todoCompleted.completed).map { it.dto() }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): Mono<Void> = todoService.delete(id)
}


fun TodoRequest.toTodo(): Todo = Todo(title = this.title, completed = false)

data class TodoCompleted(

    val completed: Boolean

)


data class TodoRequest(

    val title: String
)

data class TodoDto(

    val id: String?,

    val title: String,

    val completed: Boolean,

    val regDate: LocalDateTime
)

fun Todo.dto() = TodoDto(id = this.id, title = this.title, completed = this.completed, regDate = this.regDate)