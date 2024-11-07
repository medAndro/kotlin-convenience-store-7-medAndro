package store.domain

import camp.nextstep.edu.missionutils.Console
import store.common.Messages.*
import store.view.OutputView

class InputValidater(
    private val productRepository: ProductRepository
) {

    fun validateItems(input: String): Map<String, Int> {
        require(input.isNotBlank()) { INVALID_INPUT.inputErrorMessage() }

        val splitInputText = validateSplitInputText(input)
        val primitiveParsedMap = parseInputToMap(splitInputText)
        return validateParsedMap(primitiveParsedMap, productRepository.getAllProductQuantity())
    }


    private fun validateSplitInputText(input: String): List<String> {
        val splitInput = input.split(',')
        require(splitInput.all { element ->
            element.startsWith('[') && element.endsWith(']') && element.count { it == '-' } == 1
        }) { INVALID_INPUT.inputErrorMessage() }

        return splitInput
    }

    private fun parseInputToMap(input: List<String>): Map<String, String> {
        val pairs = input
            .map { it.removeSurrounding("[", "]") }
            .map { it.split('-').let { parts -> parts[0] to parts[1] } }
        val keys = pairs.map { it.first }
        require(keys.size == keys.distinct().size) { INVALID_INPUT_ERROR.inputErrorMessage() }

        return pairs.toMap()
    }

    private fun validateParsedMap(parsedMap: Map<String, String>, productMap: Map<String, Int>): Map<String, Int> {
        require(parsedMap.keys.size == parsedMap.keys.distinct().size) { INVALID_INPUT_ERROR.inputErrorMessage() }
        return parsedMap.entries.associate { (key, value) ->
            require(key in productMap.keys) { NOT_EXIST_PRODUCT.inputErrorMessage() }
            val buyQuantity = value.toIntOrNull()
                ?: throw IllegalArgumentException(INVALID_INPUT_ERROR.inputErrorMessage())
            val stock = productMap.getValue(key)
            require(buyQuantity <= stock) { OVERFLOW_QUANTITY.inputErrorMessage() }
            key to buyQuantity
        }
    }

    fun validateYN(input: String): Boolean {
        if (input.uppercase() == "Y") {
            return true
        }
        if (input.uppercase() == "N") {
            return false
        }
        return false
    }
}