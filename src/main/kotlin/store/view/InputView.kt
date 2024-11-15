package store.view

import camp.nextstep.edu.missionutils.Console
import store.common.Messages
import store.common.Messages.INVALID_ERROR
import store.domain.InputValidator

class InputView(
    private val outputView: OutputView,
    private val inputValidator: InputValidator
) {

    private fun <T> readUntilValidInput(validator: (String) -> T): T {
        while (true) {
            try {
                val input = validator(readLine())
                outputView.printBlankLine()
                return input
            } catch (e: IllegalArgumentException) {
                outputView.printMessage(e.message ?: INVALID_ERROR.errorMessage())
            }
        }
    }

    fun readValidItem(): Map<String, Int> {
        outputView.printMessage(Messages.INPUT_PRODUCT_NAME_QUANTITY.message())
        return readUntilValidInput { input ->
            inputValidator.validateItems(input)
        }
    }

    fun readValidYN(infoMessage: String): Boolean {
        outputView.printMessage(infoMessage)
        return readUntilValidInput { input ->
            inputValidator.validateYN(input)
        }
    }

    private fun readLine(): String = Console.readLine()
}
