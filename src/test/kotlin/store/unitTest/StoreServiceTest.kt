package store.unitTest

import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.common.AppConfig
import store.domain.InputValidator
import store.domain.InventoryService
import store.domain.ProductRepository
import store.domain.StoreService
import store.model.Product
import store.model.Promotion
import store.model.Receipt
import store.view.InputView
import store.view.OutputView

class StoreServiceTest : NsTest() {
    private lateinit var products: LinkedHashMap<String, Product>
    private lateinit var promotions: LinkedHashMap<String, Promotion>
    private lateinit var inventoryService: InventoryService
    private lateinit var productRepository: ProductRepository

    private lateinit var inputValidator: InputValidator
    private lateinit var inputView: InputView

    private lateinit var storeService: StoreService


    @BeforeEach
    fun setUp() {
        inventoryService = InventoryService()
        products = inventoryService.loadMergedProducts(AppConfig.PRODUCTS_FILE.value)
        promotions = inventoryService.loadPromotions(AppConfig.PROMOTIONS_FILE.value)

        productRepository = ProductRepository(products, promotions, Receipt())
        inputValidator = InputValidator(productRepository)
        inputView = InputView(OutputView(), inputValidator)


        storeService = StoreService(inputView, productRepository)
    }

    @Test
    fun `프로모션 상품이 없는 appendReceiptByProductName 테스트`() {
        storeService.appendReceiptByProductName("물", 5)

        val receipt = productRepository.getReceipt()
        val receiptProducts = receipt.getTotalProduct()

        assertEquals(1, receiptProducts.size)
        assertEquals("물", receiptProducts[0].getName())
        assertEquals(5, receiptProducts[0].getQuantity())
    }

    @Test
    fun `프로모션 상품이 있는 appendReceiptByProductName 테스트`() {
        assertSimpleTest {
            run("Y", "Y")
            storeService.appendReceiptByProductName("콜라", 15)

            val receipt = productRepository.getReceipt()
            val receiptProducts = receipt.getTotalProduct()

            assertEquals(1, receiptProducts.size)
            assertEquals("콜라", receiptProducts[0].getName())
            assertEquals(15, receiptProducts[0].getQuantity())
        }
    }

    @Test
    fun `readMembershipFlag true 테스트`() {
        assertSimpleTest {
            run("Y", "Y") // 멤버십 할인 받음
            storeService.appendReceiptByProductName("콜라", 1)
            storeService.readMembershipFlag()


            val receipt = productRepository.getReceipt()
            // (콜라1000 * 할인비율(0.3)) = -300
            assertThat(receipt.getFullReceiptText()).contains(
                "멤버십할인\t\t\t-300"
            )
        }
    }

    @Test
    fun `readMembershipFlag false 테스트`() {
        assertSimpleTest {
            run("Y", "N") // 멤버십 할인 받지 않음
            storeService.appendReceiptByProductName("콜라", 1)
            storeService.readMembershipFlag()

            val receipt = productRepository.getReceipt()
            assertThat(receipt.getFullReceiptText()).contains(
                "멤버십할인\t\t\t-0"
            )
        }
    }

    @Test
    fun `isExtraPurchases false 테스트`() {
        assertSimpleTest {
            run("N")
            val result = storeService.isExtraPurchases()
            assertFalse(result)
        }
    }

    @Test
    fun `isExtraPurchases true 테스트`() {
        assertSimpleTest {
            run("Y")
            val result = storeService.isExtraPurchases()
            assertTrue(result)
        }
    }

    override fun runMain() {}

}
