package store.view

import store.dto.ProductDto
import store.common.Messages.*
import store.common.commaFormat

class OutputView {
    fun printProduct(dto: ProductDto) {
        if (dto.promoQuantity > 0) {
            printPromoProduct(dto)
        }
        if (dto.quantity > 0){
            printNormalProduct(dto)
        }
        if (dto.promoQuantity > 0 && dto.quantity == 0){
            printSoldOutProduct(dto)
        }
    }

    private fun printPromoProduct(dto: ProductDto) {
        println(
            PRODUCT_PROMO_FORMAT.formattedMessage(
                dto.name,
                dto.price.commaFormat(),
                dto.promoQuantity.commaFormat(),
                dto.promotion ?: ""
            )
        )
    }

    private fun printNormalProduct(dto: ProductDto) {
        println(
            PRODUCT_FORMAT.formattedMessage(
                dto.name,
                dto.price.commaFormat(),
                dto.quantity.commaFormat()
            )
        )
    }

    private fun printSoldOutProduct(dto: ProductDto) {
        println(
            PRODUCT_SOLD_OUT_FORMAT.formattedMessage(
                dto.name,
                dto.price.commaFormat()
            )
        )
    }

    fun printBlankLine() {
        println()
    }

    fun printMessage(message: String) {
        println(message)
    }
}