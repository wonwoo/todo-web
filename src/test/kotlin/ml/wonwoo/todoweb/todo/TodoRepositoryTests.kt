package ml.wonwoo.todoweb.todo

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager


@DataJpaTest
internal class TodoRepositoryTests(private val todoRepository: TodoRepository,
                                   private val testEntityManager: TestEntityManager) {

    @Test
    fun `dummy repository test`() {

        val todo1 = testEntityManager.persist(Todo(title = "test todo", completed = true))
        val todo2 = testEntityManager.persist(Todo(title = "test todo1", completed = false))

        val todo = todoRepository.findAll()

        assertThat(todo).hasSize(2)
        assertThat(todo[0].id).isEqualTo(todo1.id)
        assertThat(todo[0].title).isEqualTo("test todo")
        assertThat(todo[0].completed).isEqualTo(true)

        assertThat(todo[1].id).isEqualTo(todo2.id)
        assertThat(todo[1].title).isEqualTo("test todo1")
        assertThat(todo[1].completed).isEqualTo(false)

    }

}