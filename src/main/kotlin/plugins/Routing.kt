package scrapeit.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(positions: List<String>) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/a-paixao-do-visconde") {
            call.respondText(positions.toString())
        }
    }
}

