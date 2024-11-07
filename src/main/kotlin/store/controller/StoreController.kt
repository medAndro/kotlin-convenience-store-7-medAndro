package store.controller

import store.common.AppConfig
import store.domain.InputValidator
import store.domain.InventoryService
import store.domain.StoreService
import store.model.NumberBasket
import store.common.Messages.*
import store.view.InputView
import store.view.OutputView

class StoreController(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val validator: InputValidator,
    inventoryService: InventoryService,
    private val storeService: StoreService
) {
    private val products = inventoryService.loadMergedProducts(AppConfig.PRODUCTS_FILE.value)
    private val promotions = inventoryService.loadPromotions(AppConfig.PROMOTIONS_FILE.value)

    fun orderProducts() {
        showProducts()
    }

    private fun showProducts() {
        outputView.printMessage(WELCOME_ANNOUNCE.message())
        products.forEach {
            outputView.printProduct(it.toDto())
        }
        outputView.printBlankLine()
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