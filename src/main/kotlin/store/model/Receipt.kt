package store.model

import store.common.Messages.*

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
    fun getFullReceiptText(): String {
        var receiptText = "==============W 편의점================\n상품명\t\t수량\t금액\n"
        var totalAmount = 0
        var totalPrice = 0
        var discountPrice = 0
        totalProduct.forEach {
            receiptText += "${it.getName()}\t\t ${it.getQuantity()}\t\t ${it.getPrice()*it.getQuantity()}\n"
            totalAmount+=it.getQuantity()
            totalPrice+= it.getPrice()*it.getQuantity()
        }
        if (promoProduct.size > 0) {
            receiptText += "=============증\t정===============\n"
        }
        promoProduct.forEach {
            receiptText += "${it.getName()}\t\t ${it.getQuantity()}\n"
            discountPrice += it.getPrice()*it.getQuantity()
        }
        receiptText += "====================================\n"
        receiptText += "총구매액\t\t$totalAmount\t$totalPrice\n"
        receiptText += "행사할인\t\t\t-$discountPrice\n"

        if (membershipFlag) {
            receiptText += "멤버십할인\t\t\t-${totalPrice-totalEventPrice}\n"
        }
        receiptText += "내실돈\t\t\t몰루\n"
        return receiptText
    }

}