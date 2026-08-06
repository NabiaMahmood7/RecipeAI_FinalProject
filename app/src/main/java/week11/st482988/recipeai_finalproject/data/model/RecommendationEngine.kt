package week11.st482988.recipeai_finalproject.data.repository

import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.data.model.RecipeRecommendationResult
import week11.st482988.recipeai_finalproject.data.model.UserPreferences
import week11.st482988.recipeai_finalproject.data.model.RecommendationWeights



class RecommendationEngine {

    fun generateRecommendations(
        allRecipes: List<Recipe>,
        userPreferences: UserPreferences,
        viewedRecipes: List<String> = emptyList(),
        favoriteRecipes: List<String> = emptyList(),
        topN: Int = 5
    ): RecipeRecommendationResult {

        val scoredRecipes = allRecipes.map { recipe ->
            val score = scoreRecipe(
                recipe = recipe,
                userPreferences = userPreferences,
                viewedRecipes = viewedRecipes,
                favoriteRecipes = favoriteRecipes
            )
            recipe.id to score
        }

        val topRecipes = scoredRecipes
            .sortedByDescending { it.second }
            .take(topN)

        return RecipeRecommendationResult(recipes = topRecipes)
    }

    private fun scoreRecipe(
        recipe: Recipe,
        userPreferences: UserPreferences,
        viewedRecipes: List<String>,
        favoriteRecipes: List<String>
    ): Double {

        // Factor 1: Dietary Match (30%)
        val dietaryMatch = if (userPreferences.dietaryPreferences.isEmpty()) {
            100
        } else {
            val matchCount = recipe.dietaryRestrictions.count {
                it in userPreferences.dietaryPreferences
            }
            if (recipe.dietaryRestrictions.isEmpty()) 100
            else ((matchCount.toDouble() / recipe.dietaryRestrictions.size) * 100).toInt()
        }

        // Factor 2: Popularity (20%)
        val popularityScore = RecommendationWeights.getPopularityScore(
            recipe.rating,
            recipe.reviewCount
        )

        // Factor 3: Skill Level (10%)
        val skillLevelScore = RecommendationWeights.getSkillLevelScore(
            userPreferences.cookingSkillLevel,
            recipe.difficulty
        )

        // Factor 4: Similarity (25%)
        val similarityScore = RecommendationWeights.getSimilarityScore(
            recipe.ingredients,
            emptyList()
        )

        // Factor 5: Diversity (15%)
        val diversityScore = RecommendationWeights.getDiversityScore(
            recipe.id,
            viewedRecipes
        )

        return RecommendationWeights.calculateFinalScore(
            dietaryMatch = dietaryMatch,
            popularityScore = popularityScore,
            similarityScore = similarityScore,
            diversityScore = diversityScore,
            skillLevelScore = skillLevelScore
        )
    }
}