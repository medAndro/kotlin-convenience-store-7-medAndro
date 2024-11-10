package store.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReceiptTest {

    private lateinit var receipt: Receipt
    private lateinit var pepero: Product
    private lateinit var pokey: Product

    @BeforeEach
    fun setUp() {
        receipt = Receipt()
        pepero = Product("빼뺴로", 1000, -1, 30, null)
        pokey = Product("포키", 1000, -1, 2, "투플러스원")
    }

    @Test
    fun `전체 영수증 출력 테스트`() {
        receipt.addTotalProduct(pepero)
        receipt.addTotalProduct(pokey)

        assertThat(receipt.getFullReceiptText()).contains(
            "==============W 편의점================",
            "빼뺴로\t\t30 \t30,000",
            "포키\t\t2 \t2,000",
            "====================================",
            "총구매액\t\t32\t32,000",
            "행사할인\t\t\t-0",
            "멤버십할인\t\t\t-0",
            "내실돈\t\t\t 32,000"
        )
    }

    @Test
    fun `TotalProduct 항목 추가 확인 테스트`() {
        receipt.addTotalProduct(pepero)
        assertTrue(receipt.getTotalProduct().contains(pepero))
    }

    @Test
    fun `PromoProduct 객체 추가 테스트`() {
        receipt.addPromoProduct(pokey)
        assertFalse(receipt.getTotalProduct().contains(pokey))
    }


    @Test
    fun `멤버십 테스트 (8000원 초과)`() {
        receipt.setMembershipFlag(true)
        receipt.addTotalProduct(pepero) //전체금액 30000
        receipt.addEventPrice(1000) //프로모션이벤트 판매금액

        // ((전체금액 - EventPrice) * 할인비율(0.3)) = 8700 -> 8000원이 최대
        assertThat(receipt.getFullReceiptText()).contains(
            "멤버십할인\t\t\t-8,000"
        )
    }

    @Test
    fun `멤버십 테스트 (8000원 이하)`() {
        receipt.setMembershipFlag(true)
        receipt.addTotalProduct(pokey) //전체금액 2000
        receipt.addEventPrice(1000) //프로모션이벤트 판매금액

        // ((전체금액 - EventPrice) * 할인비율(0.3)) = 300
        assertThat(receipt.getFullReceiptText()).contains(
            "멤버십할인\t\t\t-300"
        )
    }
}