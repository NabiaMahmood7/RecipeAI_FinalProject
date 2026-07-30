package week11.st482988.recipeai_finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st482988.recipeai_finalproject.data.model.User

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getUserProfile(userId: String): Flow<User?> = callbackFlow {
        val subscription = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(User::class.java)
                trySend(user)
            }

        awaitClose { subscription.remove() }
    }

    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addFavoriteRecipe(userId: String, recipeId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("favorites", com.google.firebase.firestore.FieldValue.arrayUnion(recipeId))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavoriteRecipe(userId: String, recipeId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("favorites", com.google.firebase.firestore.FieldValue.arrayRemove(recipeId))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}