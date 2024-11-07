package store.domain

import store.model.Product
import store.model.Promotion
import store.model.Receipt

class ProductRepository(
    private var products: LinkedHashMap<String, Product>,
    private var promotions: LinkedHashMap<String, Promotion>,
    private var receipt: Receipt = Receipt()
) {
    fun getProducts(): LinkedHashMap<String, Product> = products
    fun getPromotions(): LinkedHashMap<String, Promotion> = promotions

    fun getAllProductQuantity(): Map<String, Int> {
        return products.values.associate { product ->
            product.getName() to product.getQuantity() + product.getPromoQuantity()
        }
    }

    fun getPromoByPromoName(promoName: String): Promotion? {

        return promotions[promoName]
    }
    fun getBuyByPromoName(promoName: String): Int? {

        return promotions[promoName]?.getBuy()
    }
    fun getGetByPromoName(promoName: String): Int? {

        return promotions[promoName]?.getGet()
    }

    fun getReceipt(): Receipt {
        return receipt
    }

    fun addReceipt(product: Product, totalProductAmount: Int, bonusProductAmount: Int) {
        receipt.addPromoProduct(
            Product(product.getName(), product.getPrice(), -1, bonusProductAmount, null)
        )
        receipt.addTotalProduct(
            Product(product.getName(), product.getPrice(), -1, totalProductAmount, null)
        )
    }

    fun clearReceipt() {
        receipt = Receipt()
    }
}