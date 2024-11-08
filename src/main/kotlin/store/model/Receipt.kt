package store.model

import store.common.Messages.*
import store.common.commaFormat

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

    fun getTotalProduct(): MutableList<Product> = totalProduct

    fun getFullReceiptText(): String {
        var receiptText = "==============W 편의점================\n상품명\t\t수량\t금액\n"
        var totalAmount = 0
        var totalPrice = 0
        var discountPrice = 0

        totalProduct.forEach {
            receiptText += "${it.getName()}\t\t ${(it.getQuantity()).commaFormat()}\t\t ${(it.getPrice()*it.getQuantity()).commaFormat()}\n"
            totalAmount+=it.getQuantity()
            totalPrice+= it.getPrice()*it.getQuantity()
        }

        if (promoProduct.any { it.getQuantity() > 0 }) {
            receiptText += "=============증\t정===============\n"
        }
        promoProduct.forEach {
            if (it.getQuantity() > 0) {
                receiptText += "${it.getName()}\t\t ${(it.getQuantity()).commaFormat()}\n"
                discountPrice += it.getPrice()*it.getQuantity()
            }
        }
        receiptText += "====================================\n"
        receiptText += "총구매액\t\t${totalAmount.commaFormat()}\t${totalPrice.commaFormat()}\n"
        receiptText += "행사할인\t\t\t-${discountPrice.commaFormat()}\n"

        val membershipDiscount = ((totalPrice - totalEventPrice) * 0.3).toInt().coerceAtMost(8000)

        if (membershipFlag) {
            receiptText += "멤버십할인\t\t\t-${membershipDiscount.commaFormat()}\n"
            receiptText += "내실돈\t\t\t${(totalPrice-discountPrice-membershipDiscount).commaFormat()}\n"
        }else{
            receiptText += "내실돈\t\t\t${(totalPrice-discountPrice).commaFormat()}\n"
        }
        return receiptText
    }

}