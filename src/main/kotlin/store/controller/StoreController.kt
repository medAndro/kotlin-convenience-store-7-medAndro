package store.controller

import store.domain.InputValidator
import store.domain.StoreService
import store.model.NumberBasket
import store.resources.Messages.*
import store.view.InputView
import store.view.OutputView

class StoreController(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val validator: InputValidator,
    private val storeService: StoreService
) {
    fun orderProducts() {
        val numberBasket = generateNumberBasket()
        outputView.printMessage(SUM_START_HEADER.message())
        outputView.printBlankLine()

        announceSumNumbers(numberBasket)
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
            val storeService = StoreService()
            return StoreController(inputView, outputView, inputValidator, storeService)
        }
    }
}