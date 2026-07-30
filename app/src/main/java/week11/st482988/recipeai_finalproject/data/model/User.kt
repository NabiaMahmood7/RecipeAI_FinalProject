
package week11.st482988.recipeai_finalproject.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val dietaryPreferences: List<String> = emptyList(),
    val cookingSkillLevel: String = "",
    val favorites: List<String> = emptyList(),
    val inventory: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "uid" to uid,
        "email" to email,
        "fullName" to fullName,
        "dietaryPreferences" to dietaryPreferences,
        "cookingSkillLevel" to cookingSkillLevel,
        "favorites" to favorites,
        "inventory" to inventory,
        "createdAt" to createdAt
    )
}
