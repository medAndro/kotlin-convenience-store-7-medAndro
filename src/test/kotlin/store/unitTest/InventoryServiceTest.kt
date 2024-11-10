package store.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import store.common.AppConfig
import java.time.LocalDate

class InventoryServiceTest {
    private lateinit var inventoryService: InventoryService

    @BeforeEach
    fun setUp() {
        // InventoryService 초기화
        inventoryService = InventoryService()
    }

    @Test
    fun `loadMergedProducts 테스트`() {
        val products = inventoryService.loadMergedProducts(AppConfig.PRODUCTS_FILE.value)

        // 제품 개수 확인 (중복된 이름 제품 병합)
        assertEquals(11, products.size)

        val cola = products["콜라"]
        assertNotNull(cola)
        assertEquals("콜라", cola?.getName())
        assertEquals(1000, cola?.getPrice())
        assertEquals(10, cola?.getPromoQuantity())
        assertEquals(10, cola?.getQuantity())
        assertEquals("탄산2+1", cola?.getPromotionName())

        val orangeJuice = products["오렌지주스"]
        assertNotNull(orangeJuice)
        assertEquals("오렌지주스", orangeJuice?.getName())
        assertEquals(1800, orangeJuice?.getPrice())
        assertEquals(9, orangeJuice?.getPromoQuantity())
        assertEquals(0, orangeJuice?.getQuantity())
        assertEquals("MD추천상품", orangeJuice?.getPromotionName())

        val water = products["물"]
        assertNotNull(water)
        assertEquals("물", water?.getName())
        assertEquals(500, water?.getPrice())
        assertEquals(0, water?.getPromoQuantity())
        assertEquals(10, water?.getQuantity())
        assertNull(water?.getPromotionName())

        val drPepper = products["닥터페퍼"]
        assertNull(drPepper)
    }

    @Test
    fun `loadPromotions 테스트`() {
        val promotions = inventoryService.loadPromotions(AppConfig.PROMOTIONS_FILE.value)

        assertEquals(3, promotions.size)

        val promotion1 = promotions["탄산2+1"]
        assertNotNull(promotion1)
        assertEquals("탄산2+1", promotion1?.getName())
        assertEquals(2, promotion1?.getBuy())
        assertEquals(1, promotion1?.getGet())

        val startDate1 = LocalDate.parse("2024-01-01").atStartOfDay()
        val endDate1 = LocalDate.parse("2024-12-31").atStartOfDay()

        assertEquals(startDate1, promotion1?.getStartDate())
        assertEquals(endDate1, promotion1?.getEndDate())
    }
}
