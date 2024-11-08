package store.domain

import store.common.Messages
import store.view.InputView

class StoreService(
    private val inputView: InputView,
    private val productRepo: ProductRepository,
) {
    fun writeReceipt(buyProductName: String, buyQuantityOrigin: Int) {
        val product = productRepo.getProducts()[buyProductName]!!
        val promotionName = product.getPromotionName()
        var buyQuantity = buyQuantityOrigin

        val buy = productRepo.getBuyByPromoName(promotionName)
        val get = productRepo.getGetByPromoName(promotionName)
        if (buy != null && get != null) {
            val promoUnit = buy + get
            var remain = buyQuantity % promoUnit

            val isCanGetBonusbak = remain == buy && ((buyQuantity + get) <= product.getPromoQuantity())
            val isCanGetBonus = remain >= buy && ((buyQuantity + get) <= product.getPromoQuantity())
            val getFreeAmount = get - (remain-buy)
            if (isCanGetBonus) {
                val addPromoInfoMessage = Messages.INPUT_ADD_PROMOTION.ynMessage(buyProductName, getFreeAmount)
                // 증정품 추가여부 입력 상자 호출
                if(inputView.readValidYN(addPromoInfoMessage)){
                    buyQuantity += getFreeAmount
                    remain = buyQuantity % promoUnit
                }
            }

            var cantBuyPromoUnitAmount = (buyQuantity / promoUnit) - (product.getPromoQuantity() / promoUnit)
            if (cantBuyPromoUnitAmount < 0) {
                cantBuyPromoUnitAmount = 0
            }
            val canNotPromoAmount = remain+(cantBuyPromoUnitAmount*promoUnit)
            var bonusAmount = get * ((buyQuantity-canNotPromoAmount) / promoUnit)

            if (canNotPromoAmount>0) {
                val infoMessage = Messages.INPUT_NOT_DISCOUNT.ynMessage(buyProductName, canNotPromoAmount)

                // 프로모션 할인 증정이 적용되지 않아도 구매 Y/N 입력폼
                if (inputView.readValidYN(infoMessage)) {
//                    productRepo.addReceipt(product, buyQuantity, get * (buyQuantity / promoUnit), get, buy)
//                    return
                } else {
                    buyQuantity -= canNotPromoAmount
//                    productRepo.addReceipt(product, buyQuantity , bonusAmount, get, buy)
//                    return
                }
            }

            productRepo.addReceipt(product, buyQuantity, bonusAmount, get, buy)

        }else{
            productRepo.addReceipt(product, buyQuantity, 0, get,buy)
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

    fun isExtraPurchases(): Boolean {
        val infoMessage = Messages.INPUT_EXTRA_PURCHASES.ynMessage()
        return inputView.readValidYN(infoMessage)
    }

}