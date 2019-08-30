package ml.wonwoo.todoweb.todo

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller


@Controller
class TodoSenderController(private val todoService: TodoService) {

    @MessageMapping("/message")
    @SendTo("/todo/message")
    fun message(todoRequest: TodoRequest): TodoDto {

        return todoService.save(todoRequest.toTodo()).dto()

    }
}