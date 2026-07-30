
package week11.st482988.recipeai_finalproject.data.model

data class Ingredient(
    val id: String = "",
    val name: String = "",
    val quantity: String = "",
    val category: String = "",
    val expiryDate: Long? = null,
    val userId: String = ""
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "quantity" to quantity,
        "category" to category,
        "expiryDate" to expiryDate,
        "userId" to userId
    )
}

