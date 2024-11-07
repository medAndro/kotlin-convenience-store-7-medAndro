package store.domain

import store.common.Messages
import store.model.Product
import store.view.InputView

class StoreService(
    private val inputView: InputView,
    private val productRepo: ProductRepository,
) {

    fun writeReceipt(buyProductName: String, buyQuantity: Int) {
        val product = productRepo.getProducts()[buyProductName]!!
        var getFree = 0
        val promotionName = product.getPromotionName()
        if (promotionName.isNullOrBlank()) {
            return
        }
        val buy = productRepo.getBuyByPromoName(promotionName)
        val get = productRepo.getGetByPromoName(promotionName)
        if (buy != null && get != null) {
            val promoUnit = buy + get
            //추가여부 입력 기능
            val remain = buyQuantity % promoUnit

            if (remain == buy && ((buyQuantity + get) <= product.getPromoQuantity())) {
                val addPromoInfoMessage = Messages.INPUT_ADD_PROMOTION.ynMessage(buyProductName, get)
                if(inputView.readValidYN(addPromoInfoMessage)){
                    getFree += get
                }
            }

            // 일반적인 재고부족 확인기능
            if (product.getPromoQuantity() in 1..<buyQuantity) {
                val canNotPromoAmount = buyQuantity - ((product.getPromoQuantity() / promoUnit) * promoUnit)
                val infoMessage = Messages.INPUT_NOT_DISCOUNT.ynMessage(buyProductName, canNotPromoAmount)
                if (inputView.readValidYN(infoMessage)) { // 프로모션 할인이 적용되지 않아도 구매
                    val bonusProductAmount = (product.getPromoQuantity() / promoUnit)
                    productRepo.addReceipt(product, buyQuantity, bonusProductAmount, get, buy)
                    return
                } else {
                    val unitCount = (product.getPromoQuantity() / promoUnit)
                    productRepo.addReceipt(product, promoUnit * unitCount, get * unitCount, get, buy)
                    return
                }
            }

            val unitCount = ((buyQuantity + getFree) / promoUnit)
            productRepo.addReceipt(product, buyQuantity + getFree, get * unitCount, get, buy)

        }
    }

    fun readMembershipFlag(){
        val infoMessage = Messages.INPUT_MEMBERSHIP.ynMessage()
        if (inputView.readValidYN(infoMessage)){
            productRepo.setReceiptMembershipFlag(true)
        } else {
            productRepo.setReceiptMembershipFlag(false)
        }
    }

}