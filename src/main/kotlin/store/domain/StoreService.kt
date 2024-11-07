package store.domain

import store.common.Messages
import store.model.Product
import store.view.InputView

class StoreService(
    private val inputView: InputView,
    private val productRepo: ProductRepository,
) {

    fun writeReceipt(buyProductName: String, buyQuantity:Int) {
        val product = productRepo.getProducts()[buyProductName]!!
        //isOverflowPromoDiscount(product, buyProductName, buyQuantity)
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
            val boughtUnit = buyQuantity - remain

            if (remain == get && ((buyQuantity + get) <= product.getPromoQuantity())) {
                val addPromoInfoMessage = Messages.INPUT_ADD_PROMOTION.ynMessage(buyProductName, get)
                if(inputView.readValidYN(addPromoInfoMessage)){
                    getFree += get
                }
            }

            //재고부족 확인기능
            if (product.getPromoQuantity() in 1..<buyQuantity) {
                val canNotPromoAmount = buyQuantity - ((product.getPromoQuantity() / promoUnit) * promoUnit)
                val infoMessage = Messages.INPUT_NOT_DISCOUNT.ynMessage(buyProductName, canNotPromoAmount)
                if (inputView.readValidYN(infoMessage)) { // 프로모션 할인이 적용되지 않아도 구매
                    val bonusProductAmount = (product.getPromoQuantity() / promoUnit)
                    productRepo.addReceipt(product, buyQuantity, bonusProductAmount)
                } else {
                    val unitCount = (product.getPromoQuantity() / promoUnit)
                    productRepo.addReceipt(product, promoUnit * unitCount, get * unitCount)
                }

            }
            val unitCount = ((buyQuantity + getFree) / promoUnit)
            productRepo.addReceipt(product, buyQuantity + getFree, get * unitCount)
        }
//        if (product.getPromoQuantity() > quantity) {
//            checkPromotion(
//                getPromotions()[product.getPromotion()],
//                quantity = quantity,
//            )
//        }.

    }

//    private fun checkPromotion(promotion: Promotion?, quantity:Int): Promotion {
//        // 현재 dd 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
//    }
}