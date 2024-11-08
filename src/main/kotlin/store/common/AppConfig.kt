package store.common

enum class AppConfig(val value: String) {
    PRODUCTS_FILE("src/main/resources/products.md"),
    PROMOTIONS_FILE("src/main/resources/promotions.md"),

    MEMBERSHIP_MAXIMUM_DISCOUNT("8000"),
    MEMBERSHIP_DISCOUNT_RATIO("0.3");

    fun toInt(): Int = value.toInt()
    fun toDouble(): Double = value.toDouble()
}