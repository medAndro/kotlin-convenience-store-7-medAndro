package store.domain

import store.model.Products
import store.model.Promotions
import java.io.File
import java.time.LocalDate

class InventoryService {

    fun loadMergedProducts(filePath: String): List<Products> {
        return mergeProducts(loadProducts(filePath))
    }

    private fun loadProducts(filePath: String): List<Products> =
        readCsvLines(filePath) { line ->
            val (name, price, quantity, promotion) = line.split(",")
            Products(
                name = name,
                price = price.toInt(),
                promoQuantity = if (promotion != "null") quantity.toInt() else 0,
                quantity = if (promotion == "null") quantity.toInt() else 0,
                promotion = promotion.takeUnless { it == "null" }
            )
        }

    private fun mergeProducts(products: List<Products>): List<Products> =
        products.groupBy { it.getName() }.map { (name, groupedProducts) ->
            val first = groupedProducts.first()
            Products(
                name = name,
                price = first.getPrice(),
                promoQuantity = groupedProducts.sumOf { it.getPromoQuantity() },
                quantity = groupedProducts.sumOf { it.getQuantity() },
                promotion = groupedProducts.firstNotNullOfOrNull { it.getPromotion() }
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