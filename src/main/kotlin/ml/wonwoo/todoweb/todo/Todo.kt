package ml.wonwoo.todoweb.todo

import java.time.LocalDateTime

data class Todo(

    val id: String? = null,

    val title: String,

    var completed: Boolean,

    val regDate: LocalDateTime = LocalDateTime.now()

)