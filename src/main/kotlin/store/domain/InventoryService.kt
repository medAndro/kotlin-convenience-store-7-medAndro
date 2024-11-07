package store.domain

import store.model.Product
import store.model.Promotion
import java.io.File
import java.time.LocalDate

class InventoryService {

    fun loadMergedProducts(filePath: String): LinkedHashMap<String, Product> {
        val products = mergeProducts(loadProducts(filePath))
        return linkedMapOf(*products.map { it.getName() to it}.toTypedArray())
    }

    private fun loadProducts(filePath: String): List<Product> =
        readCsvLines(filePath) { line ->
            val (name, price, quantity, promotion) = line.split(",")
            Product(
                name = name,
                price = price.toInt(),
                promoQuantity = if (promotion != "null") quantity.toInt() else 0,
                quantity = if (promotion == "null") quantity.toInt() else 0,
                promotion = promotion.takeUnless { it == "null" }
            )
        }

    private fun mergeProducts(products: List<Product>): List<Product> =
        products.groupBy { it.getName() }.map { (name, groupedProducts) ->
            val first = groupedProducts.first()
            Product(
                name = name,
                price = first.getPrice(),
                promoQuantity = groupedProducts.sumOf { it.getPromoQuantity() },
                quantity = groupedProducts.sumOf { it.getQuantity() },
                promotion = groupedProducts.firstNotNullOfOrNull { it.getPromotionName() }
            )
        }


    fun loadPromotions(filePath: String): LinkedHashMap<String, Promotion> {
        val promotions = readPromotionsFromFile(filePath)
        return linkedMapOf(*promotions.map { it.getName() to it}.toTypedArray())
    }

    private fun readPromotionsFromFile(filePath: String): List<Promotion> {
        return readCsvLines(filePath) { line ->
            val (name, buy, get, startDate, endDate) = line.split(",")
            Promotion(
                name = name,
                buy = buy.toInt(),
                get = get.toInt(),
                startDate = LocalDate.parse(startDate).atStartOfDay(),
                endDate = LocalDate.parse(endDate).atStartOfDay()
            )
        }
    }


    private fun <T> readCsvLines(filePath: String, mapper: (String) -> T): List<T> =
        File(filePath).readLines().drop(1).map(mapper)
}