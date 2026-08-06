package week11.st482988.recipeai_finalproject.data.model


data class RecipeFeatures(
    val recipeId: String,
    val title: String,
    val matchesDietaryPreferences: Boolean,
    val skillLevelMatch: Int,
    val calorieAlignment: Int,
    val popularityScore: Int,
    val similarityScore: Int,
    val diversityScore: Int,
    val finalScore: Double
) {
    fun isGoodMatch(): Boolean = finalScore >= 60.0
}

data class RecipeRecommendationResult(
    val recipes: List<Pair<String, Double>>,
    val generatedAt: Long = System.currentTimeMillis(),
    val algorithm: String = "Hybrid Recommendation Engine v1"
)