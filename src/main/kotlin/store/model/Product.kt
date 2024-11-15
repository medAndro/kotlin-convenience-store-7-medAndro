package store.model

import store.dto.ProductDto

class Product(
    private val name: String,
    private val price: Int,
    private var promoQuantity: Int,
    private var quantity: Int,
    private val promotion: String?
) {
    fun toDto(): ProductDto = ProductDto(
        name = name,
        price = price,
        promoQuantity = promoQuantity,
        quantity = quantity,
        promotion = promotion
    )

    fun getName() = name
    fun getPrice() = price
    fun getPromoQuantity() = promoQuantity
    fun getQuantity() = quantity
    fun getPromotionName() = promotion

    fun setQuantityReduce(reduceQuantity: Int) {
        val remainingQuantity = reducePromoQuantity(reduceQuantity)
        reduceNormalQuantity(remainingQuantity)
    }

    private fun reducePromoQuantity(reduceQuantity: Int): Int {
        val promoReduction = minOf(promoQuantity, reduceQuantity)
        promoQuantity -= promoReduction

        return reduceQuantity - promoReduction
    }

    private fun reduceNormalQuantity(remainingQuantity: Int) {
        if (remainingQuantity > 0) {
            quantity -= remainingQuantity
        }
    }
}