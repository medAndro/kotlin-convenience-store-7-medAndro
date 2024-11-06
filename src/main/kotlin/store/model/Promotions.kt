package store.model

import java.time.LocalDateTime

data class Promotions(
    val name: String,
    val buy: Int,
    val get: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)