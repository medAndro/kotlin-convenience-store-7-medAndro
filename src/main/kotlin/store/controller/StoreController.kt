package store.controller

import store.common.AppConfig
import store.domain.InputValidater
import store.domain.InventoryService
import store.domain.StoreService
import store.common.Messages.*
import store.domain.ProductRepository
import store.view.InputView
import store.view.OutputView

class StoreController(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val storeService: StoreService,
    private val productRepository: ProductRepository
) {
    fun startConvenienceStore() {
        orderProducts()
        readExtraPurchasesYn()
    }

    private fun orderProducts() {
        showStock()
        val boughtProductMap = inputView.readValidItem()
        readPurchasedInfo(boughtProductMap)
        storeService.readMembershipFlag()
        println(productRepository.getReceipt().getFullReceiptText())
        productRepository.updateReceiptStock()
    }

    private fun showStock() {
        outputView.printMessage(WELCOME_ANNOUNCE.message())
        productRepository.getProducts().forEach {
            outputView.printProduct(it.value.toDto())
        }
        outputView.printBlankLine()
    }

    private fun readPurchasedInfo(boughtProductMap: Map<String, Int>) {
        boughtProductMap.forEach { (key, value) ->
            storeService.writeReceipt(key, value)
        }
    }

    private fun readExtraPurchasesYn() {
        if (storeService.isExtraPurchases()) {
            productRepository.clearReceipt()
            orderProducts()
            readExtraPurchasesYn()
        }
    }

    companion object {
        fun create() = StoreControllerBuilder().build()

        private class StoreControllerBuilder {
            private val outputView = OutputView()
            private val inventoryService = InventoryService()
            private val productRepository = initializeProductRepository(inventoryService)
            private val inputValidater = InputValidater(productRepository)
            private val inputView = InputView(outputView, inputValidater)
            private val storeService = StoreService(inputView, productRepository)

            fun build() = StoreController(
                inputView,
                outputView,
                storeService,
                productRepository
            )

            private fun initializeProductRepository(inventoryService: InventoryService) =
                ProductRepository(
                    inventoryService.loadMergedProducts(AppConfig.PRODUCTS_FILE.value),
                    inventoryService.loadPromotions(AppConfig.PROMOTIONS_FILE.value)
                )
        }
    }
}