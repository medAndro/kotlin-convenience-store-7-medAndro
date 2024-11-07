package store.view

import camp.nextstep.edu.missionutils.Console
import store.common.Messages
import store.common.Messages.INVALID_ERROR
import store.domain.InputValidater

class InputView(
    private val outputView: OutputView,
    private val inputValidater: InputValidater
) {

    private fun <T> readUntilValidInput(validator: (String) -> T): T {
        while (true) {
            try {
                val input = validator(readLine())
                return input
            } catch (e: IllegalArgumentException) {
                outputView.printMessage(e.message ?: INVALID_ERROR.errorMessage())
            }
        }
    }

    fun readValidItem(): Map<String, Int> {
        outputView.printMessage(Messages.INPUT_PRODUCT_NAME_QUANTITY.message())
        return readUntilValidInput() { input ->
            inputValidater.validateItems(input)
        }
    }

    fun readValidYN(): Boolean {
        outputView.printMessage(Messages.INPUT_PRODUCT_NAME_QUANTITY.message())
        return readUntilValidInput() { input ->
            inputValidater.validateYN(input)
        }

    }



    fun readLine(): String = Console.readLine()
}
