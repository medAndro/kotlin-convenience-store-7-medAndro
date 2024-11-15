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

    fun getAllProductQuantity(): Map<String, Int> {
        return products.values.associate { product ->
            product.getName() to product.getQuantity() + product.getPromoQuantity()
        }
    }

    fun getBuyGetPairByPromoName(promoName: String?): Pair<Int?, Int?> {
        return Pair(
            promotions[promoName]?.getBuy(),
            promotions[promoName]?.getGet()
        )
    }

    fun setReceiptMembershipFlag(flag: Boolean) {
        receipt.setMembershipFlag(flag)
    }

    fun addReceipt(product: Product, totalProductAmount: Int, bonusProductAmount: Int, get: Int?, buy: Int?) {
        addProductsToReceipt(product, totalProductAmount, bonusProductAmount)
        addEventPriceIfApplicable(product, bonusProductAmount, get, buy)
    }

    private fun addProductsToReceipt(product: Product, totalProductAmount: Int, bonusProductAmount: Int) {
        receipt.addPromoProduct(
            Product(product.getName(), product.getPrice(), -1, bonusProductAmount, null)
        )
        receipt.addTotalProduct(
            Product(product.getName(), product.getPrice(), -1, totalProductAmount, null)
        )
    }

    private fun addEventPriceIfApplicable(product: Product, bonusProductAmount: Int, get: Int?, buy: Int?) {
        if (get != null && buy != null) {
            val eventPrice = (bonusProductAmount / get) * (get + buy) * product.getPrice()
            receipt.addEventPrice(eventPrice)
        }
    }

    fun getReceipt(): Receipt {
        return receipt
    }

    fun updateStockByReceipt() {
        val totalBoughtProduct = receipt.getTotalProduct()
        totalBoughtProduct.forEach { boughtProduct ->
            products[boughtProduct.getName()]?.let { product ->
                val boughtQuantity = boughtProduct.getQuantity()
                product.setQuantityReduce(boughtQuantity)
            }
        }
    }

    fun clearReceipt() {
        receipt = Receipt()
    }
}