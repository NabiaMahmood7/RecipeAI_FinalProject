package week11.st482988.recipeai_finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.data.model.RecipeRecommendationResult
import week11.st482988.recipeai_finalproject.data.model.UserPreferences

class RecipeRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val recommendationEngine = RecommendationEngine()

    // ========================
    // ORIGINAL METHODS
    // ========================

    suspend fun getAllRecipes(): Result<List<Recipe>> {
        return try {
            val snapshot = firestore.collection("recipes").get().await()
            val recipes = snapshot.documents.mapNotNull {
                it.toObject(Recipe::class.java)
            }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeById(recipeId: String): Result<Recipe> {
        return try {
            val doc = firestore.collection("recipes").document(recipeId).get().await()
            val recipe = doc.toObject(Recipe::class.java)
            if (recipe != null) {
                Result.success(recipe)
            } else {
                Result.failure(Exception("Recipe not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addRecipe(recipe: Recipe): Result<String> {
        return try {
            val docRef = firestore.collection("recipes").add(recipe).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRecipe(recipeId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("recipes").document(recipeId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return try {
            firestore.collection("recipes").document(recipeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========================
    // NEW ML METHODS
    // ========================

    suspend fun getPersonalizedRecommendations(
        userId: String,
        userPreferences: UserPreferences,
        topN: Int = 5
    ): Result<RecipeRecommendationResult> {
        return try {
            val allRecipesSnapshot = firestore.collection("recipes").get().await()
            val allRecipes = allRecipesSnapshot.documents.mapNotNull {
                it.toObject(Recipe::class.java)
            }

            val userViewedRecipes = getUserViewedRecipes(userId)
            val userFavoriteRecipes = getUserFavoriteRecipes(userId)

            val recommendations = recommendationEngine.generateRecommendations(
                allRecipes = allRecipes,
                userPreferences = userPreferences,
                viewedRecipes = userViewedRecipes,
                favoriteRecipes = userFavoriteRecipes,
                topN = topN
            )

            Result.success(recommendations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrendingRecipes(topN: Int = 5): Result<List<Recipe>> {
        return try {
            val snapshot = firestore.collection("recipes")
                .orderBy("rating", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(topN.toLong())
                .get()
                .await()

            val recipes = snapshot.documents.mapNotNull {
                it.toObject(Recipe::class.java)
            }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recordRecipeView(userId: String, recipeId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("viewedRecipes",
                    com.google.firebase.firestore.FieldValue.arrayUnion(recipeId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========================
    // HELPER METHODS
    // ========================

    private suspend fun getUserViewedRecipes(userId: String): List<String> {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            @Suppress("UNCHECKED_CAST")
            (doc.get("viewedRecipes") as? List<String>) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getUserFavoriteRecipes(userId: String): List<String> {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            @Suppress("UNCHECKED_CAST")
            (doc.get("favorites") as? List<String>) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}