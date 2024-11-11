package store.unitTest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.common.AppConfig
import store.domain.InventoryService
import store.domain.ProductRepository
import store.model.Product
import store.model.Promotion
import store.model.Receipt

class ProductRepositoryTest {

    private lateinit var products: LinkedHashMap<String, Product>
    private lateinit var promotions: LinkedHashMap<String, Promotion>
    private lateinit var receipt: Receipt
    private lateinit var inventoryService: InventoryService
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        inventoryService = InventoryService()
        products = inventoryService.loadMergedProducts(AppConfig.PRODUCTS_FILE.value)
        promotions = inventoryService.loadPromotions(AppConfig.PROMOTIONS_FILE.value)
        receipt = Receipt()

        productRepository = ProductRepository(products, promotions, receipt)
    }

    @Test
    fun `getAllProductQuantity 테스트`() {
        val productQuantities = productRepository.getAllProductQuantity()

        assertEquals(20, productQuantities["콜라"])  // (10(프로모션) + 10)
        assertEquals(15, productQuantities["사이다"])  // (8(프로모션) + 7)
        assertEquals(10, productQuantities["물"])  // 10
    }

    @Test
    fun `getBuyGetPairByPromoName 테스트`() {
        val promoPair = productRepository.getBuyGetPairByPromoName("탄산2+1")

        assertEquals(2, promoPair.first)  // buy
        assertEquals(1, promoPair.second)  // get

        val nonExistentPromo = productRepository.getBuyGetPairByPromoName("99% 세일")
        assertNull(nonExistentPromo.first)  // 존재하지 않는 프로모션
        assertNull(nonExistentPromo.second)
    }

    @Test
    fun `setReceiptMembershipFlag 테스트`() {
        productRepository.setReceiptMembershipFlag(false)
        val pepero = Product("빼뺴로", 1000, -1, 30, null)
        receipt.setMembershipFlag(true)
        receipt.addTotalProduct(pepero) // 전체금액 1000*30
        receipt.addEventPrice(1000) // 프로모션 판매금액

        // ((전체금액 - EventPrice) * 할인비율(0.3)) = 8700 -> 8000원이 최대
        assertThat(receipt.getFullReceiptText()).contains(
            "멤버십할인\t\t\t-8,000"
        )
    }

    @Test
    fun `addReceipt 테스트`() {
        val cola = products["콜라"]!!
        productRepository.addReceipt(cola, totalProductAmount = 5, bonusProductAmount = 2, get = 1, buy = 2)
        val receiptProducts = receipt.getTotalProduct()
        assertEquals(5, receiptProducts.first().getQuantity())
    }

    @Test
    fun `updateReceiptStock 테스트`() {
        val cola = products["콜라"]!!
        // 영수증 5개 판매
        productRepository.addReceipt(cola, totalProductAmount = 5, bonusProductAmount = 0, get = null, buy = null)

        // 재고 업데이트 전 콜라의 수량 (20개)
        assertEquals(20, cola.getQuantity() + cola.getPromoQuantity())

        productRepository.updateStockByReceipt()
        // 재고 업데이트 후 콜라의 수량 (20-5개)
        assertEquals(15, cola.getQuantity() + cola.getPromoQuantity())
    }

    @Test
    fun `clearReceipt 테스트`() {
        val cola = products["콜라"]!!
        productRepository.addReceipt(cola, totalProductAmount = 5, bonusProductAmount = 0, get = null, buy = null)
        assertEquals(1, productRepository.getReceipt().getTotalProduct().size)

        productRepository.clearReceipt()
        assertEquals(0, productRepository.getReceipt().getTotalProduct().size)  // 영수증이 비워져야 함
    }
}
