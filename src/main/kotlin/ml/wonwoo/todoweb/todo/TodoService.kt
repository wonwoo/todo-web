package ml.wonwoo.todoweb.todo

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class TodoService(private val todoRepository: TodoRepository) {

    fun findAll(): Flux<Todo> = todoRepository.findAll()

    fun save(todo: Todo) = todoRepository.save(todo)

    fun completed(id: String, completed: Boolean) = todoRepository.findById(id)
        .doOnNext {

            it.completed = completed

        }.flatMap { todoRepository.save(it) }.log()

    fun delete(id: String) = todoRepository.deleteById(id)

}