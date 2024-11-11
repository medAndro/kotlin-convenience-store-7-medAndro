package store.domain

import store.common.Messages
import store.view.InputView
import store.model.Product

class StoreService(
    private val inputView: InputView,
    private val productRepo: ProductRepository,
) {
    fun appendReceiptByProductName(buyProductName: String, buyQuantityOrigin: Int) {
        val product = productRepo.getProducts()[buyProductName]
        if (product != null) processReceipt(product, buyQuantityOrigin)
    }

    private fun processReceipt(product: Product, buyQuantityOrigin: Int) {
        val promotionInfo = productRepo.getBuyGetPairByPromoName(product.getPromotionName())
        if (promotionInfo.first == null) {
            productRepo.addReceipt(product, buyQuantityOrigin, 0, null, null)
            return
        }
        processPromotionalProduct(product, buyQuantityOrigin, promotionInfo)
    }

    private fun processPromotionalProduct(product: Product, buyQuantityOrigin: Int, promotionInfo: Pair<Int?, Int?>) {
        val (buy, get) = promotionInfo
        requireNotNull(buy)
        requireNotNull(get)

        val adjustedQuantity = calculatePromotionalQuantity(product, buyQuantityOrigin, buy, get)
        val (finalQuantity, bonusAmount) = calculateFinalAmounts(product, adjustedQuantity, buy, get)

        productRepo.addReceipt(product, finalQuantity, bonusAmount, get, buy)
    }

    private fun calculatePromotionalQuantity(product: Product, buyQuantity: Int, buy: Int, get: Int): Int {
        val promoUnit = buy + get
        val remain = buyQuantity % promoUnit
        if (remain >= buy && ((buyQuantity + get) <= product.getPromoQuantity())) {
            val freeAmount = get - (remain - buy)
            if (inputView.readValidYN(Messages.INPUT_ADD_PROMOTION.ynMessage(product.getName(), freeAmount))) {
                return buyQuantity + freeAmount
            }
        }
        return buyQuantity
    }

    private fun calculateFinalAmounts(product: Product, buyQuantity: Int, buy: Int, get: Int): Pair<Int, Int> {
        val nonPromotionalAmount = calculateNonPromotionalAmount(product, buyQuantity, buy, get)
        val finalQuantity = adjustQuantityByUserInput(product, buyQuantity, nonPromotionalAmount)
        return calculateBonusAmount(buyQuantity, finalQuantity, nonPromotionalAmount, buy, get)
    }

    private fun calculateNonPromotionalAmount(product: Product, buyQuantity: Int, buy: Int, get: Int): Int {
        val promoUnit = buy + get
        val remain = buyQuantity % promoUnit
        val nonPromotionalUnits = ((buyQuantity / promoUnit) - (product.getPromoQuantity() / promoUnit))
            .coerceAtLeast(0)
        return remain + (nonPromotionalUnits * promoUnit)
    }

    private fun adjustQuantityByUserInput(product: Product, buyQuantity: Int, nonPromotionalAmount: Int): Int {
        if (nonPromotionalAmount == 0) return buyQuantity
        val message = Messages.INPUT_NOT_DISCOUNT.ynMessage(product.getName(), nonPromotionalAmount)
        if (inputView.readValidYN(message)) return buyQuantity
        return buyQuantity - nonPromotionalAmount
    }

    private fun calculateBonusAmount(
        buyQuantity: Int, finalQuantity: Int, nonPromotionalAmount: Int, buy: Int, get: Int
    ): Pair<Int, Int> {
        val promoUnit = buy + get
        val bonusAmount = get * ((buyQuantity - nonPromotionalAmount) / promoUnit)

        return Pair(finalQuantity, bonusAmount)
    }

    fun readMembershipFlag() {
        val infoMessage = Messages.INPUT_MEMBERSHIP.ynMessage()
        val isMembership = inputView.readValidYN(infoMessage)
        productRepo.setReceiptMembershipFlag(isMembership)
    }

    fun isExtraPurchases(): Boolean {
        val infoMessage = Messages.INPUT_EXTRA_PURCHASES.ynMessage()
        return inputView.readValidYN(infoMessage)
    }
}