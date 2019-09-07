package ml.wonwoo.todoweb.todo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TodoRepository : ReactiveMongoRepository<Todo, String>