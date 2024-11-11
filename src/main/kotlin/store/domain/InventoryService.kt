package store.domain

import store.common.AppConfig
import store.model.Product
import store.model.Promotion
import java.io.File
import java.time.LocalDate
import camp.nextstep.edu.missionutils.DateTimes
import java.time.LocalDateTime

class InventoryService {
    fun loadMergedProducts(filePath: String): LinkedHashMap<String, Product> {
        val products = mergeProducts(loadProducts(filePath))
        return linkedMapOf(*products.map { it.getName() to it }.toTypedArray())
    }

    private fun loadProducts(filePath: String): List<Product> =
        readCsvLines(filePath) { line ->
            createProductFromFileLine(line, loadPromotions(AppConfig.PROMOTIONS_FILE.value))
        }

    private fun createProductFromFileLine(fileLine: String, promotionMap: Map<String, Promotion>): Product {
        val (name, price, quantity, promotion) = fileLine.split(",")
        return createProductByPromotion(name, price, quantity, promotion, promotionMap)
    }

    private fun createProductByPromotion(
        name: String, price: String, quantity: String, promotion: String, promotionMap: Map<String, Promotion>
    ): Product {
        val isValidPromotion = isValidPromotionDate(
            promotion,
            promotionMap[promotion]?.getStartDate(),
            promotionMap[promotion]?.getEndDate()
        )
        if (!isValidPromotion) return createNormalProduct(name, price, quantity)
        return createPromotionalProduct(name, price, quantity, promotion)
    }

    private fun isValidPromotionDate(
        promotion: String, startDate: LocalDateTime?, endDate: LocalDateTime?
    ): Boolean {
        if (promotion == "null") return false
        if (startDate == null || endDate == null) return false

        val now = DateTimes.now()
        return now in startDate..endDate
    }

    private fun createPromotionalProduct(
        name: String, price: String, quantity: String, promotion: String
    ): Product = Product(
        name = name,
        price = price.toInt(),
        promoQuantity = calculatePromoQuantity(promotion, quantity),
        quantity = calculateNormalQuantity(promotion, quantity),
        promotion = promotion.takeUnless { it == "null" }
    )

    private fun calculatePromoQuantity(promotion: String, quantity: String): Int {
        if (promotion != "null") return quantity.toInt()
        return 0
    }

    private fun calculateNormalQuantity(promotion: String, quantity: String): Int {
        if (promotion == "null") return quantity.toInt()
        return 0
    }

    private fun createNormalProduct(
        name: String, price: String, quantity: String
    ): Product = Product(
        name = name,
        price = price.toInt(),
        promoQuantity = 0,
        quantity = quantity.toInt(),
        promotion = null
    )

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
        return linkedMapOf(*promotions.map { it.getName() to it }.toTypedArray())
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