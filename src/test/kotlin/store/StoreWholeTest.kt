package store

import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class StoreWholeTest : NsTest() {
    @Test
    fun `실행 결과 예시 테스트`() {
        assertSimpleTest {
            // given
            run("[콜라-3],[에너지바-5]", "Y", "Y", "[콜라-10]", "Y", "N", "Y", "[오렌지주스-1]", "Y", "Y", "N")
            // then
            val expected = """
            안녕하세요. W편의점입니다.
            현재 보유하고 있는 상품입니다.
            
            - 콜라 1,000원 10개 탄산2+1
            - 콜라 1,000원 10개
            - 사이다 1,000원 8개 탄산2+1
            - 사이다 1,000원 7개
            - 오렌지주스 1,800원 9개 MD추천상품
            - 오렌지주스 1,800원 재고 없음
            - 탄산수 1,200원 5개 탄산2+1
            - 탄산수 1,200원 재고 없음
            - 물 500원 10개
            - 비타민워터 1,500원 6개
            - 감자칩 1,500원 5개 반짝할인
            - 감자칩 1,500원 5개
            - 초코바 1,200원 5개 MD추천상품
            - 초코바 1,200원 5개
            - 에너지바 2,000원 5개
            - 정식도시락 6,400원 8개
            - 컵라면 1,700원 1개 MD추천상품
            - 컵라면 1,700원 10개
            
            구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
            
            멤버십 할인을 받으시겠습니까? (Y/N)
            
            ==============W 편의점================
            상품명		수량	금액
            콜라		3 	3,000
            에너지바 		5 	10,000
            =============증	정===============
            콜라		1
            ====================================
            총구매액		8	13,000
            행사할인			-1,000
            멤버십할인			-3,000
            내실돈			 9,000
            
            감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
            
            안녕하세요. W편의점입니다.
            현재 보유하고 있는 상품입니다.
            
            - 콜라 1,000원 7개 탄산2+1
            - 콜라 1,000원 10개
            - 사이다 1,000원 8개 탄산2+1
            - 사이다 1,000원 7개
            - 오렌지주스 1,800원 9개 MD추천상품
            - 오렌지주스 1,800원 재고 없음
            - 탄산수 1,200원 5개 탄산2+1
            - 탄산수 1,200원 재고 없음
            - 물 500원 10개
            - 비타민워터 1,500원 6개
            - 감자칩 1,500원 5개 반짝할인
            - 감자칩 1,500원 5개
            - 초코바 1,200원 5개 MD추천상품
            - 초코바 1,200원 5개
            - 에너지바 2,000원 재고 없음
            - 정식도시락 6,400원 8개
            - 컵라면 1,700원 1개 MD추천상품
            - 컵라면 1,700원 10개
            
            구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
            
            현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
            
            멤버십 할인을 받으시겠습니까? (Y/N)
            
            ==============W 편의점================
            상품명		수량	금액
            콜라		10 	10,000
            =============증	정===============
            콜라		2
            ====================================
            총구매액		10	10,000
            행사할인			-2,000
            멤버십할인			-0
            내실돈			 8,000
            
            감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
            
            안녕하세요. W편의점입니다.
            현재 보유하고 있는 상품입니다.
            
            - 콜라 1,000원 재고 없음 탄산2+1
            - 콜라 1,000원 7개
            - 사이다 1,000원 8개 탄산2+1
            - 사이다 1,000원 7개
            - 오렌지주스 1,800원 9개 MD추천상품
            - 오렌지주스 1,800원 재고 없음
            - 탄산수 1,200원 5개 탄산2+1
            - 탄산수 1,200원 재고 없음
            - 물 500원 10개
            - 비타민워터 1,500원 6개
            - 감자칩 1,500원 5개 반짝할인
            - 감자칩 1,500원 5개
            - 초코바 1,200원 5개 MD추천상품
            - 초코바 1,200원 5개
            - 에너지바 2,000원 재고 없음
            - 정식도시락 6,400원 8개
            - 컵라면 1,700원 1개 MD추천상품
            - 컵라면 1,700원 10개
            
            구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
            
            현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
            
            멤버십 할인을 받으시겠습니까? (Y/N)
            
            ==============W 편의점================
            상품명		수량	금액
            오렌지주스		2 	3,600
            =============증	정===============
            오렌지주스		1
            ====================================
            총구매액		2	3,600
            행사할인			-1,800
            멤버십할인			-0
            내실돈			 1,800
            
            감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
            """.trimIndent()

            assertThat(output().normalize()).contains(expected.normalize())
        }
    }


    private fun String.normalize(): String {
        return this.replace("\r\n", "\n").replace("\\s".toRegex(), "")
    }

    @ParameterizedTest
    @MethodSource("inputListsForExceptionTest")
    fun `예외 테스트 모음`(inputList: List<String>) {
        assertSimpleTest {
            runException(*inputList.toTypedArray())
            assertThat(output()).contains(ERROR_MESSAGE)
        }
    }

    override fun runMain() {
        main()
    }

    companion object {
        private const val ERROR_MESSAGE: String = "[ERROR]"

        @JvmStatic
        fun inputListsForExceptionTest() = listOf(
            listOf(" "),
            listOf("\n"),
            listOf("[콜라-3],[에네르기파-5]"),
            listOf("[콜라-3],[콜라-5]"),
            listOf("[콜라--1]"),
            listOf("[콜라3],[에너지바5]"),
            listOf("콜라-3,에너지바-5"),
            listOf("[콜라-3][에너지바-5]"),
            listOf("[콜라][에너지바]"),
            listOf("[3],[5]"),
            listOf("[콜라-3],[에너지바-5]", "네"),
            listOf("[콜라-3],[에너지바-5]", "Yes"),
            listOf("[콜라-3],[에너지바-5]", " "),
            listOf("[콜라-3],[에너지바-5]", "\n"),
        )
    }
}