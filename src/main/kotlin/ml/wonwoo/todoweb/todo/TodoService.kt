package ml.wonwoo.todoweb.todo

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class TodoService(private val todoRepository: TodoRepository) {

    fun findAll(): Flux<Todo> = todoRepository.findAll()

    fun save(todo: Todo) = todoRepository.save(todo)

    fun completed(id: String, completed: Boolean) = todoRepository.findById(id)

        .doOnNext {

            it.completed = completed

        }.flatMap { todoRepository.save(it) }
        .switchIfEmpty(Mono.error(TodoNotFoundException("not found id $id")))

    fun delete(id: String) = todoRepository.deleteById(id)

}