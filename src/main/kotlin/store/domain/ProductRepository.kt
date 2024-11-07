package store.domain

import store.model.Product
import store.model.Promotion

class ProductRepository(
    private var products: List<Product>,
    private var promotions: List<Promotion>
) {
    fun getProducts(): List<Product> = products
    fun getPromotions(): List<Promotion> = promotions

    fun getAllProductQuantity(): Map<String, Int> {
        return products.associate { product ->
            product.getName() to product.getQuantity() + product.getPromoQuantity()
        }
    }

}