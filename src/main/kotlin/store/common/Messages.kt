package store.common

enum class Messages(private val message: String) {
    WELCOME_ANNOUNCE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"),

    PRODUCT_FORMAT("- %s %s원 %s개"),
    LEFT_VALUE_INPUT("좌변의 값을 입력하세요"),
    RIGHT_VALUE_INPUT("우변의 값을 입력하세요"),
    SUM_RESULT(
        """
        덧셈 결과
        ---
        %s = %s
        """.trimIndent()
    ),

    ERROR("[ERROR] %s"),
    EMPTY_INPUT("입력값이 비어있습니다."),
    NOT_INTEGER("입력값이 정수가 아닙니다."),
    INVALID_ERROR("알 수 없는 오류가 발생했습니다.");

    fun message(): String = message
    fun errorMessage(): String = ERROR.formattedMessage(message)
    fun formattedMessage(vararg args: Any): String = String.format(message, *args)
}
