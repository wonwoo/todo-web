package ml.wonwoo.todoweb

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering

@Controller
class IndexController {

    @GetMapping("/")
    fun index(): Rendering = Rendering
        .redirectTo("/index.html")
        .build()

}