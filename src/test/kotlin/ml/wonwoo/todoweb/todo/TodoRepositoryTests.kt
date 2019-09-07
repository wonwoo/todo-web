package ml.wonwoo.todoweb.todo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier


@DataMongoTest
internal class TodoRepositoryTests(private val todoRepository: TodoRepository) {

    @Test
    fun `dummy repository test`() {

        val todo1 = todoRepository.saveAll(listOf(Todo(title = "test todo", completed = true), Todo(title = "test todo1", completed = false)))

        val todo = todoRepository.findAll()

        val many = todo1.thenMany(todo)

        StepVerifier.create(many).assertNext {
            assertThat(it.id).isNotNull()
            assertThat(it.title).isEqualTo("test todo")
            assertThat(it.completed).isEqualTo(true)
        }.assertNext {
            assertThat(it.id).isNotNull()
            assertThat(it.title).isEqualTo("test todo1")
            assertThat(it.completed).isEqualTo(false)
        }.verifyComplete()

    }

}