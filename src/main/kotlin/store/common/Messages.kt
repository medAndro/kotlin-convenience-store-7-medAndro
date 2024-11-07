package store.common

enum class Messages(private val message: String) {
    WELCOME_ANNOUNCE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"),

    PRODUCT_PROMO_FORMAT("- %s %s원 %s개 %s"),
    PRODUCT_FORMAT("- %s %s원 %s개"),
    PRODUCT_SOLD_OUT_FORMAT("- %s %s원 재고 없음"),

    INPUT_PRODUCT_NAME_QUANTITY("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),


//    LEFT_VALUE_INPUT("좌변의 값을 입력하세요"),
//    RIGHT_VALUE_INPUT("우변의 값을 입력하세요"),
//    SUM_RESULT(
//        """
//        덧셈 결과
//        ---
//        %s = %s
//        """.trimIndent()
//    ),

    ERROR("[ERROR] %s"),
    INPUT_ERROR("[ERROR] %s 다시 입력해 주세요."),
    INVALID_INPUT("올바르지 않은 형식으로 입력했습니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다."),
    OVERFLOW_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다."),
    INVALID_INPUT_ERROR("잘못된 입력입니다."),

    INVALID_ERROR("알 수 없는 오류가 발생했습니다.");

    fun message(): String = message
    fun errorMessage(): String = ERROR.formattedMessage(message)
    fun inputErrorMessage(): String = INPUT_ERROR.formattedMessage(message)
    fun formattedMessage(vararg args: Any): String = String.format(message, *args)
}
