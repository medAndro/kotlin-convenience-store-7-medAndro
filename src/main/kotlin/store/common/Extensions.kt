package store.common

import java.text.DecimalFormat

fun Int.commaFormat(): String =
    DecimalFormat("#,###").format(this)