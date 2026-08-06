
package week11.st482988.recipeai_finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import week11.st482988.recipeai_finalproject.data.model.User

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("User creation failed")

            user.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()
            ).await()

            val newUser = User(
                uid = user.uid,
                email = user.email ?: "",
                fullName = fullName
            )

            firestore.collection("users")
                .document(user.uid)
                .set(newUser.toMap())
                .await()

            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("Login failed")

            val firestoreUser = firestore.collection("users")
                .document(user.uid)
                .get()
                .await()
                .toObject(User::class.java) ?: User(
                uid = user.uid,
                email = user.email ?: "",
                fullName = user.displayName ?: ""
            )

            Result.success(firestoreUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): User? {
        val user = auth.currentUser ?: return null
        return User(
            uid = user.uid,
            email = user.email ?: "",
            fullName = user.displayName ?: ""
        )
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun logout(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserPreferences(
        userId: String,
        dietaryPreferences: List<String>,
        cookingSkillLevel: String
    ): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update(
                    mapOf(
                        "dietaryPreferences" to dietaryPreferences,
                        "cookingSkillLevel" to cookingSkillLevel
                    )
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
