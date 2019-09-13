package ml.wonwoo.todoweb.todo

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono


@Service
class TodoService(private val todoRepository: TodoRepository) {

    fun findAll(): Flux<Todo> = todoRepository.findAll()

    fun save(todo: Todo) = todoRepository.save(todo)

    fun completed(id: String, completed: Boolean) = todoRepository.findById(id)

        .doOnNext {

            it.completed = completed

        }.flatMap { todoRepository.save(it) }
        .switchIfEmpty { TodoNotFoundException("not found id $id").toMono() }

    fun delete(id: String) = todoRepository.deleteById(id)

}