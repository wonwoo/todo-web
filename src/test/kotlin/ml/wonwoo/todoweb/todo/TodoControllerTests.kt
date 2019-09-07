package ml.wonwoo.todoweb.todo

import ml.wonwoo.todoweb.any
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@WebFluxTest(TodoController::class)
internal class TodoControllerTests(private val webTestClient: WebTestClient) {


    @MockBean
    lateinit var todoService: TodoService

    @Test
    fun `todo find all test`() {

        given(todoService.findAll()).willReturn(

            Flux.just(Todo(title = "todo list", completed = false),
                Todo(title = "spring study", completed = true))

        )

        webTestClient.get()
            .uri("/todo")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].title").isEqualTo("todo list")
            .jsonPath("$[1].title").isEqualTo("spring study")
            .jsonPath("$[0].completed").isEqualTo(false)
            .jsonPath("$[1].completed").isEqualTo(true)


    }


    @Test
    fun `todo save test`() {

        given(todoService.save(any())).willReturn(

            Mono.just(Todo(title = "todo list", completed = false))

        )

        webTestClient.post()
            .uri("/todo")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .body("""{"title" :"todo list" , "completed" : "false"}""")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.title").isEqualTo("todo list")
            .jsonPath("$.completed").isEqualTo(false)

    }

    @Test
    fun `todo completed test`() {

        given(todoService.completed(anyString(), anyBoolean())).willReturn(

            Mono.just(Todo(title = "todo list", completed = true))

        )

        webTestClient.put()
            .uri("/todo/{id}", 1)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .body("""{"completed" : "true"}""")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.title").isEqualTo("todo list")
            .jsonPath("$.completed").isEqualTo(true)

    }

    @Test
    fun `todo delete test`() {

        given(todoService.delete(any())).willReturn(Mono.empty())

        webTestClient.delete()
            .uri("/todo/{id}", 1)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()

    }
}