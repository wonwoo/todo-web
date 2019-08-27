package ml.wonwoo.todoweb.todo

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class TodoService(private val todoRepository: TodoRepository) {

    fun findAll(): List<Todo> = todoRepository.findAll()

    @Transactional
    fun save(todo: Todo) = todoRepository.save(todo)

    @Transactional
    fun completed(id: Long, completed: Boolean) = todoRepository.findByIdOrNull(id)?.apply {

        this.completed = completed

    } ?: throw TodoNotFoundException("todo not found id : $id")

    @Transactional
    fun delete(id: Long) = todoRepository.deleteById(id)

}