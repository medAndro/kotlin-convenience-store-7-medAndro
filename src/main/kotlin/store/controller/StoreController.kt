package store.controller

import store.domain.InputValidator
import store.domain.InventoryService
import store.domain.StoreService
import store.model.NumberBasket
import store.model.Products
import store.model.Promotions
import store.resources.Messages.*
import store.view.InputView
import store.view.OutputView

class StoreController(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val validator: InputValidator,
    inventoryService: InventoryService,
    private val storeService: StoreService
) {
    private val products = inventoryService.loadProducts("src/main/resources/products.md")
    private val promotions = inventoryService.loadPromotions("src/main/resources/promotions.md")

    fun orderProducts() {

    }

    private fun generateNumberBasket(): NumberBasket {
        val basket = NumberBasket()

        basket.addNumber(readNumberWithRetry(LEFT_VALUE_INPUT.infoMessage()))
        basket.addNumber(readNumberWithRetry(RIGHT_VALUE_INPUT.infoMessage()))

        return basket
    }

    private fun readNumberWithRetry(infoMessage: String): Int {
        while (true) {
            try {
                outputView.printMessage(infoMessage)
                return validator.validateInteger(inputView.readLine())
            } catch (e: IllegalArgumentException) {
                outputView.printMessage(e.message ?: INVALID_ERROR.errorMessage())
            }
        }
    }

    private fun announceSumNumbers(numberBasket: NumberBasket) {
        val expression = storeService.getExpression(numberBasket)
        val sumValue = storeService.plusTwoNumber(numberBasket)
        outputView.printMessage(SUM_RESULT.formattedMessage(expression, sumValue))
    }

    companion object {
        fun create(): StoreController {
            val inputView = InputView()
            val outputView = OutputView()
            val inputValidator = InputValidator()
            val inventoryService = InventoryService()
            val storeService = StoreService()
            return StoreController(inputView, outputView, inputValidator, inventoryService, storeService)
        }
    }
}