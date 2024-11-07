package store.domain

import store.model.Product
import store.model.Promotion

class ProductRepository(
    private var products: LinkedHashMap<String, Product>,
    private var promotions: LinkedHashMap<String, Promotion>
) {
    fun getProducts(): LinkedHashMap<String, Product> = products
    fun getPromotions(): LinkedHashMap<String, Promotion> = promotions

    fun getAllProductQuantity(): Map<String, Int> {
        return products.values.associate { product ->
            product.getName() to product.getQuantity() + product.getPromoQuantity()
        }
    }

//    fun sellProduct(productName: String, quantity:Int): {
//        products.forEach { product ->
//            if( product.key == productName ) {
//
//            }
//        }
//    }
}