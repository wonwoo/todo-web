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
import reactor.test.StepVerifier


@ExtendWith(MockitoExtension::class)
internal class TodoServiceTests(@Mock private val todoRepository: TodoRepository) {


    private val todoService = TodoService(this.todoRepository)


    @Test
    fun `todo find all test`() {

        given(todoRepository.findAll())
            .willReturn(Flux.just(Todo(id = "foo", title = "todo list", completed = true),
                Todo(id = "bar", title = "todo list1", completed = false)))

        val todo = todoService.findAll()

        StepVerifier.create(todo).assertNext {
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
            .willReturn(Mono.just(Todo(id = "foo", title = "todo list", completed = true)))

        val todo = todoService.save(Todo(title = "todo list", completed = true))

        StepVerifier.create(todo).assertNext {
            assertThat(it.id).isEqualTo("foo")
            assertThat(it.title).isEqualTo("todo list")
            assertThat(it.completed).isEqualTo(true)
        }.verifyComplete()

    }

    @Test
    fun `todo completed test`() {

        given(todoRepository.findById(anyString()))
            .willReturn(Mono.just(Todo(id = "foo", title = "todo list", completed = true)))

        given(todoRepository.save(any<Todo>()))
            .willReturn(Mono.just(Todo(id = "foo", title = "todo list", completed = true)))

        val todo = todoService.completed("foo", false)

        StepVerifier.create(todo).assertNext {
            assertThat(it.id).isEqualTo("foo")
            assertThat(it.title).isEqualTo("todo list")
            assertThat(it.completed).isEqualTo(true)
        }.verifyComplete()

    }

    @Test
    fun `todo delete test`() {

        given(todoRepository.deleteById(anyString())).willReturn(Mono.empty())

        val delete = todoService.delete("foo")

        StepVerifier.create(delete).then {
            verify(todoRepository, atLeastOnce()).deleteById(anyString())
        }.verifyComplete()

    }
}