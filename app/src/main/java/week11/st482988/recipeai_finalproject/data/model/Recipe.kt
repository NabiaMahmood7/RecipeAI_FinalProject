
package week11.st482988.recipeai_finalproject.data.model

data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val cookingTime: Int = 0,
    val difficulty: String = "",
    val cuisine: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val mealType: String = "",
    val dietaryRestrictions: List<String> = emptyList(),
    val servings: Int = 1
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "description" to description,
        "imageUrl" to imageUrl,
        "ingredients" to ingredients,
        "instructions" to instructions,
        "cookingTime" to cookingTime,
        "difficulty" to difficulty,
        "cuisine" to cuisine,
        "rating" to rating,
        "reviewCount" to reviewCount,
        "mealType" to mealType,
        "dietaryRestrictions" to dietaryRestrictions,
        "servings" to servings
    )
}
