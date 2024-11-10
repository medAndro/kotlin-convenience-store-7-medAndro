package store.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.dto.ProductDto

class ProductTest {
    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        product = Product(
            name = "새우깡",
            price = 1500,
            promoQuantity = 10,
            quantity = 50,
            promotion = "원플러스원"
        )
    }

    @Test
    fun `toDto 테스트`() {
        val dto: ProductDto = product.toDto()
        assertEquals("새우깡", dto.name)
        assertEquals(1500, dto.price)
        assertEquals(10, dto.promoQuantity)
        assertEquals(50, dto.quantity)
        assertEquals("원플러스원", dto.promotion)
    }

    @Test
    fun `getName 테스트`() {
        assertEquals("새우깡", product.getName())
    }

    @Test
    fun `getPrice 테스트`() {
        assertEquals(1500, product.getPrice())
    }

    @Test
    fun `getPromoQuantity 테스트`() {
        assertEquals(10, product.getPromoQuantity())
    }

    @Test
    fun `getQuantity 테스트`() {
        assertEquals(50, product.getQuantity())
    }

    @Test
    fun `getPromotionName 테스트`() {
        assertEquals("원플러스원", product.getPromotionName())
    }

    @Test
    fun `setQuantityReduce 수량 감소 테스트`() {
        assertEquals(10, product.getPromoQuantity())
        assertEquals(50, product.getQuantity())

        // 프로모션 수량이 먼저 감소해야 함
        product.setQuantityReduce(5)
        assertEquals(5, product.getPromoQuantity())
        assertEquals(50, product.getQuantity())

        // 프로모션 수량은 0이 되어야 하고, 일반 수량이 감소해야 함
        product.setQuantityReduce(10)
        assertEquals(0, product.getPromoQuantity())
        assertEquals(45, product.getQuantity())
    }
}