package scrapeit.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import scrapeit.BookPositions

fun Application.configureRouting(positions: List<BookPositions>) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/a-paixao-do-visconde") {
            call.respond(positions)
        }
    }
}

