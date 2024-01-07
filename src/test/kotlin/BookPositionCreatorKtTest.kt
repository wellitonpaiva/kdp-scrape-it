package scrapeit

import kotlin.test.Test
import kotlin.test.assertEquals

class BookPositionCreatorKtTest {
    
    @Test
    fun `can parse simple entry`() {
        val rankings = listOf(
            "Ranking dos mais vendidos: Nº 1,016 em Loja Kindle ( Conheça o Top 100 na categoria Loja Kindle ) Nº 5 em Ficção histórica Nº 9 em Regência Romance Nº 9 em eBooks de Romance Histórico sobre Regência",
            "Nº 5 em Ficção histórica",
            "Nº 9 em Regência Romance",
            "Nº 9 em eBooks de Romance Histórico sobre Regência")
        val book = Book("book", "url")
        assertEquals(BookPositions(book, 1016), BookPositions.parse(book, rankings))
    }
}