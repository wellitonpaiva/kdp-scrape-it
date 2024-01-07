package scrapeit

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.eachText
import it.skrape.selects.html5.span
import scrapeit.plugins.configureRouting


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val positions = getPositionsFromBook()
    configureRouting(positions)
}

fun getPositionsFromBook(): List<String> {
    return skrape(BrowserFetcher) {
        request { url = "https://a.co/d/25b4nSh" }
        response {
            htmlDocument {
                findAll(cssSelector = ".a-list-item") {
                    span {
                        eachText
                    }
                }
            }
        }
    }.filter { it.contains("Nº") }
}