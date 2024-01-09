package scrapeit

import it.skrape.core.htmlDocument
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.eachText
import it.skrape.selects.html5.span
import kotlinx.serialization.Serializable

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