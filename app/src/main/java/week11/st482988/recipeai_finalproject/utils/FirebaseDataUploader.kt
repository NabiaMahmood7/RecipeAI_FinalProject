package week11.st482988.recipeai_finalproject.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.st482988.recipeai_finalproject.data.SampleRecipes

object FirebaseDataUploader {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun uploadSampleRecipes() {
        try {
            val recipes = SampleRecipes.getAllRecipes()

            for (recipe in recipes) {
                firestore.collection("recipes")
                    .document(recipe.id)
                    .set(recipe)
                    .await()
            }

            println("✓ Successfully uploaded ${recipes.size} recipes to Firestore")
        } catch (e: Exception) {
            println("✗ Error uploading recipes: ${e.message}")
        }
    }
}