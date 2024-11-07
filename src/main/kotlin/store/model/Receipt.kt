package store.model

class Receipt {
    private val totalProduct: MutableList<Product> = mutableListOf()
    private val promoProduct: MutableList<Product> = mutableListOf()


    fun getTotalPrice(): Int = totalProduct.sumOf { it.getPrice() }
    fun getPromoPrice(): Int = promoProduct.sumOf { it.getPrice() }

    fun addTotalProduct(product: Product){
        totalProduct += product
    }
    fun addPromoProduct(product: Product){
        promoProduct += product
    }
//    fun getMembershipDiscount() {
//
//    }
}