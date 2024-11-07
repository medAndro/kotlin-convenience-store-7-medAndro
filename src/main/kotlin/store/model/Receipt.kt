package store.model

class Receipt {
    private val totalProduct: MutableList<Product> = mutableListOf()
    private val promoProduct: MutableList<Product> = mutableListOf()
    private var totalEventPrice = 0
    private var membershipFlag = false

    fun getTotalPrice(): Int = totalProduct.sumOf { it.getPrice() }
    fun getPromoPrice(): Int = promoProduct.sumOf { it.getPrice() }

    fun addEventPrice(price: Int) {
        totalEventPrice += price
    }

    fun addTotalProduct(product: Product){
        totalProduct += product
    }

    fun addPromoProduct(product: Product){
        promoProduct += product
    }

    fun setMembershipFlag(membershipFlag: Boolean){
        this.membershipFlag = membershipFlag
    }
}