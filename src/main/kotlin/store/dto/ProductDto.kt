package store.dto

data class ProductDto(
    val name: String,
    val price: Int,
    val promoQuantity: Int,
    val quantity: Int,
    val promotion: String?
)