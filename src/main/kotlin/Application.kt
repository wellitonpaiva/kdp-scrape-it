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
import kotlinx.coroutines.runBlocking
import scrapeit.plugins.configureRouting
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.*

@Serializable
data class BookPositions(val book: Book, val generalRanking: Int) {
    companion object {
        private fun parse(book: Book, rankings: List<String>): BookPositions {
            val generalRanking = Regex("[0-9,]+").findAll(rankings[0])
                .map(MatchResult::value).first().replace(",", "").toInt()
            return BookPositions(book, generalRanking)
        }

        fun getRanking(book: Book): BookPositions = parse(book, scrapePositions(book.url))


        private fun scrapePositions(bookUrl: String): List<String> {
            return skrape(BrowserFetcher) {
                request { url = bookUrl }
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
    }
}

@Serializable
data class Book(val name: String, val url: String)

val books = listOf(Book("A Paixao do Visconde", "https://a.co/d/25b4nSh"))

val positions = books.map { BookPositions.getRanking(it) }

fun main() {
    runBlocking {
        //positionsgetPositionsFromBook()
    }
    embeddedServer(Netty, port = 8080, host = "0.0.0.0",module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {prettyPrint = true})
    }
    configureRouting(positions)
}
