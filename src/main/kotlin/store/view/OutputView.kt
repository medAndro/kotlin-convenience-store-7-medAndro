package store.view

import store.dto.ProductDto
import store.common.Messages.*
import store.common.commaFormat

class OutputView {
    fun printProduct(dto: ProductDto) {
        print(PRODUCT_FORMAT.formattedMessage(dto.name, dto.price.commaFormat(), dto.quantity.commaFormat()))
        dto.promotion?.let { print(" $it") }
        println()
    }

    fun printBlankLine() {
        println()
    }

    fun printMessage(message: String) {
        println(message)
    }
}