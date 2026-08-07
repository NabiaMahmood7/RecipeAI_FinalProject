package week11.st482988.recipeai_finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import week11.st482988.recipeai_finalproject.data.model.Review

class ReviewRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun getReviews(recipeId: String): Flow<List<Review>> = callbackFlow {
        val subscription = firestore.collection("recipes")
            .document(recipeId)
            .collection("reviews")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reviews = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Review::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(reviews)
            }

        awaitClose { subscription.remove() }
    }

    suspend fun addReview(recipeId: String, review: Review): Result<Unit> {
        return try {
            val recipeRef = firestore.collection("recipes").document(recipeId)
            val reviewRef = recipeRef.collection("reviews").document()

            firestore.runTransaction { transaction ->
                val recipeSnapshot = transaction.get(recipeRef)
                val currentRating = recipeSnapshot.getDouble("rating") ?: 0.0
                val currentCount = recipeSnapshot.getLong("reviewCount") ?: 0L

                val newCount = currentCount + 1
                val newRating = ((currentRating * currentCount) + review.rating) / newCount

                transaction.set(reviewRef, review.copy(id = reviewRef.id).toMap())
                transaction.update(recipeRef, mapOf("rating" to newRating, "reviewCount" to newCount))
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}