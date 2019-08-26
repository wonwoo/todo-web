package ml.wonwoo.todoweb.todo

import ml.wonwoo.todoweb.any
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.doNothing
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.util.Locale

@WebMvcTest(TodoController::class)
internal class TodoControllerTests(private val mockMvc: MockMvc) {


    @MockBean
    lateinit var todoService: TodoService

    @Test
    fun `todo find all test`() {

        given(todoService.findAll()).willReturn(

                listOf(Todo(title = "todo list", completed = false),
                    Todo(title = "spring study", completed = true))

            )

        mockMvc.get("/todo") {
            accept = APPLICATION_JSON
            headers {
                contentLanguage = Locale.KOREA
            }
        }.andExpect {
            status { isOk }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$[0].title") { value("todo list") }
            jsonPath("$[1].title") { value("spring study") }
            jsonPath("$[0].completed") { value(false) }
            jsonPath("$[1].completed") { value(true) }
        }.andDo {
            print()
        }
    }


    @Test
    fun `todo save test`() {

        given(todoService.save(any())).willReturn(

            Todo(title = "todo list", completed = false)

        )

        mockMvc.post("/todo") {
            param("title", "todo list")
            param("completed", "false")
            accept = APPLICATION_JSON
            headers {
                contentLanguage = Locale.KOREA
            }
        }.andExpect {
            status { isOk }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$.title") { value("todo list") }
            jsonPath("$.completed") { value(false) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `todo completed test` () {

        given(todoService.completed(anyLong(), anyBoolean())).willReturn(

            Todo(title = "todo list", completed = true)

        )

        mockMvc.put("/todo/{id}", 1) {
            param("completed", "true")
            accept = APPLICATION_JSON
            headers {
                contentLanguage = Locale.KOREA
            }
        }.andExpect {
            status { isOk }
            content { contentType(APPLICATION_JSON) }
            jsonPath("$.title") { value("todo list") }
            jsonPath("$.completed") { value(true) }
        }.andDo {
            print()
        }
    }

    @Test
    fun `todo delete test`() {

        doNothing()
            .`when`(todoService).delete(anyLong())

        mockMvc.delete("/todo/{id}", 1) {
            accept = APPLICATION_JSON
            headers {
                contentLanguage = Locale.KOREA
            }
        }.andExpect {
            status { isOk }
        }.andDo {
            print()
        }
    }
}