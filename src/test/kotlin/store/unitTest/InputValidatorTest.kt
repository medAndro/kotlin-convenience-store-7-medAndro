package store.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import store.common.AppConfig

class InputValidatorTest {
    private lateinit var inputValidator: InputValidator
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        val inventoryService = InventoryService()
        productRepository = ProductRepository(
            inventoryService.loadMergedProducts(AppConfig.PRODUCTS_FILE.value),
            inventoryService.loadPromotions(AppConfig.PROMOTIONS_FILE.value)
        )

        inputValidator = InputValidator(productRepository)
    }

    @Test
    fun `정상적인 입력 테스트`() {
        val input = "[콜라-2],[사이다-3]"
        val result = inputValidator.validateItems(input)

        val expected = mapOf(
            "콜라" to 2,
            "사이다" to 3
        )

        assertEquals(expected, result)
    }

    @Test
    fun `입력에 대괄호가 빠진 경우 테스트`() {
        val input = "콜라-2,사이다-3"

        val exception = assertThrows<IllegalArgumentException> {
            inputValidator.validateItems(input)
        }

        assertEquals("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", exception.message)
    }

    @Test
    fun `중복 입력 테스트`() {
        val input = "[콜라-2],[콜라-3]" // 중복된 제품 코드

        val exception = assertThrows<IllegalArgumentException> {
            inputValidator.validateItems(input)
        }

        assertEquals("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.", exception.message)
    }

    @Test
    fun `존재하지 않는 상품 테스트`() {
        val input = "[닥터페퍼-2]"

        val exception = assertThrows<IllegalArgumentException> {
            inputValidator.validateItems(input)
        }

        assertEquals("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.", exception.message)
    }

    @Test
    fun `재고보다 많은 수량 테스트`() {
        val input = "[콜라-25]"

        val exception = assertThrows<IllegalArgumentException> {
            inputValidator.validateItems(input)
        }
        assertEquals("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.", exception.message)
    }

    @Test
    fun `YN입력 대문자 테스트`() {
        val result = inputValidator.validateYN("Y")
        assertTrue(result)
    }

    @Test
    fun `YN입력 소문자 테스트`() {
        val result = inputValidator.validateYN("n")
        assertFalse(result)
    }

    @Test
    fun `유효하지 않은 YN입력 테스트`() {
        val exception = assertThrows<IllegalArgumentException> {
            inputValidator.validateYN("네")
        }

        assertEquals("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.", exception.message)
    }
}