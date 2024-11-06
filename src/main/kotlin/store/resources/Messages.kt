package store.resources

enum class Messages(private val message: String) {
    INFO("[INFO] %s"),
    SUM_START_HEADER("덧셈을 시작합니다"),
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
    fun infoMessage(): String = INFO.formattedMessage(message)
    fun errorMessage(): String = ERROR.formattedMessage(message)
    fun formattedMessage(vararg args: Any): String = String.format(message, *args)
}
