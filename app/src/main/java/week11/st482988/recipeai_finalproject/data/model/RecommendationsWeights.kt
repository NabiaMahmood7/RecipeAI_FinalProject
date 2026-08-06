package week11.st482988.recipeai_finalproject.data.model

object RecommendationWeights {
    // Weights (must sum to 1.0 = 100%)
    const val DIETARY_MATCH_WEIGHT = 0.30      // 30%
    const val POPULARITY_WEIGHT = 0.20         // 20%
    const val SIMILARITY_WEIGHT = 0.25         // 25%
    const val DIVERSITY_WEIGHT = 0.15          // 15%
    const val SKILL_LEVEL_WEIGHT = 0.10        // 10%

    // Skill level matching
    fun getSkillLevelScore(userLevel: String, recipeLevel: String): Int {
        return when {
            userLevel == recipeLevel -> 100
            userLevel == "Advanced" -> 100
            userLevel == "Intermediate" && recipeLevel != "Advanced" -> 90
            userLevel == "Beginner" && recipeLevel == "Easy" -> 100
            userLevel == "Beginner" && recipeLevel == "Intermediate" -> 60
            else -> 40
        }
    }

    // Calorie alignment
    fun getCalorieScore(userPreferredCalories: Int, recipeCalories: Int): Int {
        val difference = kotlin.math.abs(userPreferredCalories - recipeCalories)
        return when {
            difference < 100 -> 100
            difference < 200 -> 80
            difference < 300 -> 60
            difference < 400 -> 40
            else -> 20
        }
    }

    // Popularity based on rating + reviews
    fun getPopularityScore(rating: Double, reviewCount: Int): Int {
        val ratingScore = (rating / 5.0 * 100).toInt()
        val reviewScore = kotlin.math.min((reviewCount / 200 * 50).toInt(), 50)
        return (ratingScore * 0.7 + reviewScore * 0.3).toInt()
    }

    // Similarity based on common ingredients
    fun getSimilarityScore(
        recipeIngredients: List<String>,
        userFavoriteIngredients: List<String>
    ): Int {
        if (userFavoriteIngredients.isEmpty()) return 50
        val commonCount = recipeIngredients.count { it in userFavoriteIngredients }
        val similarityRatio = commonCount.toDouble() / recipeIngredients.size
        return (similarityRatio * 100).toInt()
    }

    // Diversity to avoid repetition
    fun getDiversityScore(
        recipeId: String,
        userViewedRecipes: List<String>,
        maxRecentViewThreshold: Int = 10
    ): Int {
        val recentViews = userViewedRecipes.takeLast(maxRecentViewThreshold)
        return if (recipeId !in recentViews) 100 else 30
    }

    // Final weighted score
    fun calculateFinalScore(
        dietaryMatch: Int,
        popularityScore: Int,
        similarityScore: Int,
        diversityScore: Int,
        skillLevelScore: Int
    ): Double {
        return (
                dietaryMatch * DIETARY_MATCH_WEIGHT +
                        popularityScore * POPULARITY_WEIGHT +
                        similarityScore * SIMILARITY_WEIGHT +
                        diversityScore * DIVERSITY_WEIGHT +
                        skillLevelScore * SKILL_LEVEL_WEIGHT
                )
    }
}