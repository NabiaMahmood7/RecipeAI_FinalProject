package week11.st482988.recipeai_finalproject.data.model

data class Review(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "userId" to userId,
        "userName" to userName,
        "rating" to rating,
        "comment" to comment,
        "createdAt" to createdAt
    )
}