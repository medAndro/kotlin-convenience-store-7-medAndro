package store.model

import store.dto.ProductDto

class Products(
    val name: String,
    val price: Int,
    private var quantity: Int,
    val promotion: String?
) {
    fun toDto(): ProductDto = ProductDto(
        name = name,
        price = price,
        quantity = quantity,
        promotion = promotion
    )
}