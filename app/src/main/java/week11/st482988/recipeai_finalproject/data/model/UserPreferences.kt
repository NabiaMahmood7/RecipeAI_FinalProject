
package week11.st482988.recipeai_finalproject.data.model

data class UserPreferences(
    val uid: String = "",
    val dietaryRestrictions: List<String> = emptyList(),
    val cookingSkillLevel: String = "",
    val dislikedCuisines: List<String> = emptyList(),
    val favoriteIngredients: List<String> = emptyList()
)
