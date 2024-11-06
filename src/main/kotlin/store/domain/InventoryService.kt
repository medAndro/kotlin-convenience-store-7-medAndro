package store.domain

import store.model.Products
import store.model.Promotions
import java.io.File
import java.time.LocalDate

class InventoryService {
    fun loadProducts(filePath: String): List<Products> =
        readCsvLines(filePath) { line ->
            val (name, price, quantity, promotion) = line.split(",")
            Products(
                name = name,
                price = price.toInt(),
                quantity = quantity.toInt(),
                promotion = promotion.takeUnless { it == "null" }
            )
        }

    fun loadPromotions(filePath: String): List<Promotions> =
        readCsvLines(filePath) { line ->
            val (name, buy, get, startDate, endDate) = line.split(",")
            Promotions(
                name = name,
                buy = buy.toInt(),
                get = get.toInt(),
                startDate = LocalDate.parse(startDate).atStartOfDay(),
                endDate = LocalDate.parse(endDate).atStartOfDay()
            )
        }

    private fun <T> readCsvLines(filePath: String, mapper: (String) -> T): List<T> =
        File(filePath).readLines().drop(1).map(mapper)
}