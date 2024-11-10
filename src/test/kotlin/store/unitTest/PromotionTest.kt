package store.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class PromotionTest {
    private lateinit var promotion: Promotion

    @BeforeEach
    fun setUp() {
        promotion = Promotion(
            name = "투플러스원",
            buy = 2,
            get = 1,
            startDate = LocalDate.parse("2024-01-01").atStartOfDay(),
            endDate = LocalDate.parse("2025-01-01").atStartOfDay()
        )
    }

    @Test
    fun `getName 테스트`() {
        assertEquals("투플러스원", promotion.getName())
    }

    @Test
    fun `getBuy 테스트`() {
        assertEquals(2, promotion.getBuy())
    }

    @Test
    fun `getGet 테스트`() {
        assertEquals(1, promotion.getGet())
    }

    @Test
    fun `getStartDate 테스트`() {
        val expectedStartDate = LocalDate.parse("2024-01-01").atStartOfDay()
        assertEquals(expectedStartDate, promotion.getStartDate())
    }

    @Test
    fun `getEndDate 테스트`() {
        val expectedEndDate = LocalDate.parse("2025-01-01").atStartOfDay()
        assertEquals(expectedEndDate, promotion.getEndDate())
    }
}