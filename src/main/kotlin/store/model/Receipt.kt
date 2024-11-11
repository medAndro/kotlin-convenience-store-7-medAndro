package store.model

import store.common.AppConfig.*
import store.common.Messages.*
import store.common.commaFormat

class Receipt {
    private val totalProduct: MutableList<Product> = mutableListOf()
    private val promoProduct: MutableList<Product> = mutableListOf()
    private var totalEventPrice = 0
    private var membershipFlag = false

    fun addEventPrice(price: Int) {
        totalEventPrice += price
    }

    fun addTotalProduct(product: Product) {
        totalProduct += product
    }

    fun addPromoProduct(product: Product) {
        promoProduct += product
    }

    fun setMembershipFlag(membershipFlag: Boolean) {
        this.membershipFlag = membershipFlag
    }

    fun getTotalProduct(): MutableList<Product> = totalProduct

    fun getFullReceiptText(): String {
        val (totalAmount, totalPrice, discountPrice) = calculateTotalAmounts()
        val membershipDiscount = calculateMembershipDiscount(totalPrice)

        return buildString {
            append(createReceiptHeader())
            append(createPromotionSection())
            append(createTotalSection(totalAmount, totalPrice, discountPrice, membershipDiscount))
        }
    }

    private fun calculateTotalAmounts(): Triple<Int, Int, Int> {
        val totalAmount = totalProduct.sumOf { it.getQuantity() }
        val totalPrice = totalProduct.sumOf { it.getPrice() * it.getQuantity() }
        val discountPrice = promoProduct
            .filter { it.getQuantity() > 0 }
            .sumOf { it.getPrice() * it.getQuantity() }

        return Triple(totalAmount, totalPrice, discountPrice)
    }

    private fun calculateMembershipDiscount(totalPrice: Int): Int =
        ((totalPrice - totalEventPrice) * MEMBERSHIP_DISCOUNT_RATIO.toDouble()).toInt()
            .coerceAtMost(MEMBERSHIP_MAXIMUM_DISCOUNT.toInt())

    private fun createReceiptHeader(): String = buildString {
        append(RECEIPT_HEADER.message())
        totalProduct.forEach {
            append(formatProductLine(it))
        }
    }

    private fun formatProductLine(product: Product): String =
        "${product.getName()}\t\t${product.getQuantity().commaFormat()} \t${calculateProductAmount(product)}\n"

    private fun calculateProductAmount(product: Product): String =
        (product.getPrice() * product.getQuantity()).commaFormat()

    private fun createPromotionSection(): String = buildString {
        if (promoProduct.any { it.getQuantity() > 0 }) {
            append(RECEIPT_PROMOTION_HEADER.message())
            promoProduct.filter { it.getQuantity() > 0 }
                .forEach {
                    append("${it.getName()}\t\t${it.getQuantity().commaFormat()}\n")
                }
        }
        append(RECEIPT_FOOTER.message())
    }

    private fun createTotalSection(
        totalAmount: Int, totalPrice: Int, discountPrice: Int, membershipDiscount: Int
    ): String = buildString {
        append(RECEIPT_TOTAL.formattedMessage(totalAmount.commaFormat(), totalPrice.commaFormat()))
        append(RECEIPT_EVENT_DISCOUNT.formattedMessage(discountPrice.commaFormat()))
        append(RECEIPT_MEMBERSHIP_DISCOUNT.formattedMessage(getMembershipDiscountCommaText(membershipDiscount)))
        append(RECEIPT_FINAL_AMOUNT.formattedMessage(getFinalAmount(totalPrice, discountPrice, membershipDiscount)))
    }

    private fun getMembershipDiscountCommaText(membershipDiscount: Int): String {
        if (membershipFlag) return membershipDiscount.commaFormat()
        return "0"
    }

    private fun getFinalAmount(totalPrice: Int, discountPrice: Int, membershipDiscount: Int): String =
        (totalPrice - discountPrice - getMembershipDiscountNumber(membershipDiscount)).commaFormat()

    private fun getMembershipDiscountNumber(membershipDiscount: Int): Int {
        if (membershipFlag) return membershipDiscount
        return 0
    }
}