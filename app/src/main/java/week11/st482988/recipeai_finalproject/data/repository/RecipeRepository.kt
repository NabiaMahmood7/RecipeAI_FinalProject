package week11.st482988.recipeai_finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st482988.recipeai_finalproject.data.model.Recipe

class RecipeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getAllRecipes(): Flow<List<Recipe>> = callbackFlow {
        val subscription = firestore.collection("recipes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val recipes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Recipe::class.java)
                } ?: emptyList()

                trySend(recipes)
            }

        awaitClose { subscription.remove() }
    }


    suspend fun getRecipeById(recipeId: String): Result<Recipe> {
        return try {
            val doc = firestore.collection("recipes")
                .document(recipeId)
                .get()
                .await()

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


    suspend fun searchRecipes(query: String): Result<List<Recipe>> {
        return try {
            val snapshot = firestore.collection("recipes")
                .whereArrayContains("ingredients", query.lowercase())
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


    suspend fun getRecipesByDifficulty(difficulty: String): Result<List<Recipe>> {
        return try {
            val snapshot = firestore.collection("recipes")
                .whereEqualTo("difficulty", difficulty)
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

}