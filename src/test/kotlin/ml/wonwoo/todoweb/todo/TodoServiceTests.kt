package ml.wonwoo.todoweb.todo

import ml.wonwoo.todoweb.any
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import reactor.kotlin.test.test


@ExtendWith(MockitoExtension::class)
internal class TodoServiceTests(@Mock private val todoRepository: TodoRepository) {


    private val todoService = TodoService(this.todoRepository)


    @Test
    fun `todo find all test`() {

        given(todoRepository.findAll())
            .willReturn(listOf(Todo(id = "foo", title = "todo list", completed = true),
                Todo(id = "bar", title = "todo list1", completed = false)).toFlux())

        val todo = todoService.findAll()

        todo.test().assertNext {
            assertThat(it.id).isEqualTo("foo")
            assertThat(it.title).isEqualTo("todo list")
            assertThat(it.completed).isEqualTo(true)
        }.assertNext {
            assertThat(it.id).isEqualTo("bar")
            assertThat(it.title).isEqualTo("todo list1")
            assertThat(it.completed).isEqualTo(false)
        }.verifyComplete()
    }


    @Test
    fun `todo save test`() {

        given(todoRepository.save(any<Todo>()))
            .willReturn(Todo(id = "foo", title = "todo list", completed = true).toMono())

        val todo = todoService.save(Todo(title = "todo list", completed = true))

        todo.test().assertNext {
            assertThat(it.id).isEqualTo("foo")
            assertThat(it.title).isEqualTo("todo list")
            assertThat(it.completed).isEqualTo(true)
        }.verifyComplete()

    }

    @Test
    fun `todo completed test`() {

        given(todoRepository.findById(anyString()))
            .willReturn(Todo(id = "foo", title = "todo list", completed = true).toMono())

        given(todoRepository.save(any<Todo>()))
            .willReturn(Todo(id = "foo", title = "todo list", completed = true).toMono())

        val todo = todoService.completed("foo", false)

        todo.test().assertNext {
            assertThat(it.id).isEqualTo("foo")
            assertThat(it.title).isEqualTo("todo list")
            assertThat(it.completed).isEqualTo(true)
        }.verifyComplete()

    }

    @Test
    fun `todo completed not found id test`() {

        given(todoRepository.findById(anyString()))
            .willReturn(Mono.empty())

        val todo = todoService.completed("foo", false)

        todo.test()

            .expectErrorSatisfies {

                assertThat(it).isInstanceOf(TodoNotFoundException::class.java)
                assertThat(it.message).isEqualTo("not found id foo")

            }
            .verify()

    }

    @Test
    fun `todo delete test`() {

        given(todoRepository.deleteById(anyString())).willReturn(Mono.empty())

        val delete = todoService.delete("foo")

        delete.test().then {
            verify(todoRepository, atLeastOnce()).deleteById(anyString())
        }.verifyComplete()

    }
}