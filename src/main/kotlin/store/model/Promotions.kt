package store.model

import java.time.LocalDateTime

class Promotions(
    private val name: String,
    private val buy: Int,
    private val get: Int,
    private val startDate: LocalDateTime,
    private val endDate: LocalDateTime
)