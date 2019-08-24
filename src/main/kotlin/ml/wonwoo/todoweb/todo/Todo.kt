package ml.wonwoo.todoweb.todo

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Todo(

    @Id
    @GeneratedValue
    val id: Long? = null,

    val title: String,

    var completed: Boolean,

    val regDate: LocalDateTime = LocalDateTime.now()

)