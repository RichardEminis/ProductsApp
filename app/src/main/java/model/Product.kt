package model

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val price: Int,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val images: List<String>,
)