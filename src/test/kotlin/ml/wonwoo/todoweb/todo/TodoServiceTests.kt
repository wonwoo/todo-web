package ml.wonwoo.todoweb.todo

import ml.wonwoo.todoweb.any
import ml.wonwoo.todoweb.assertThatExceptionOfType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.doNothing
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional


@ExtendWith(MockitoExtension::class)
internal class TodoServiceTests(@Mock private val todoRepository: TodoRepository) {


    private val todoService = TodoService(this.todoRepository)


    @Test
    fun `todo find all test`() {

        given(todoRepository.findAll())
            .willReturn(listOf(Todo(id = 1L, title = "todo list", completed = true),
                Todo(id = 2L, title = "todo list1", completed = false)))

        val todo = todoService.findAll()

        assertThat(todo).hasSize(2)
        assertThat(todo[0].id).isEqualTo(1)
        assertThat(todo[0].title).isEqualTo("todo list")
        assertThat(todo[0].completed).isEqualTo(true)

        assertThat(todo[1].id).isEqualTo(2)
        assertThat(todo[1].title).isEqualTo("todo list1")
        assertThat(todo[1].completed).isEqualTo(false)
    }


    @Test
    fun `todo save test`() {

        given(todoRepository.save(any<Todo>()))
            .willReturn(Todo(id = 1L, title = "todo list", completed = true))

        val todo = todoService.save(Todo(title = "todo list", completed = true))

        assertThat(todo.id).isEqualTo(1)
        assertThat(todo.title).isEqualTo("todo list")
        assertThat(todo.completed).isEqualTo(true)

    }

    @Test
    fun `todo completed test`() {

        given(todoRepository.findById(anyLong()))
            .willReturn(Optional.of(Todo(id = 1L, title = "todo list", completed = true)))

        val todo = todoService.completed(1, false)

        assertThat(todo.id).isEqualTo(1)
        assertThat(todo.title).isEqualTo("todo list")
        assertThat(todo.completed).isEqualTo(false)

    }

    @Test
    fun `todo completed find all not found test`() {

        given(todoRepository.findById(anyLong()))
            .willReturn(Optional.empty())

        assertThatExceptionOfType<NullPointerException>()
            .isThrownBy {
                todoService.completed(1, false)
            }.withMessage("todo null")

    }

    @Test
    fun `todo delete test`() {

        doNothing().`when`(todoRepository).deleteById(any())

        todoService.delete(1)

        verify(todoRepository, atLeastOnce()).deleteById(any())

    }
}