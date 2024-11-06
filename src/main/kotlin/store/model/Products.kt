package store.model

data class Products(
    val name: String,
    val price: Int,
    var quantity: Int,
    val promotion: String?
)