package week11.st482988.recipeai_finalproject.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import week11.st482988.recipeai_finalproject.BuildConfig
import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.data.model.User
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class Recommendation(val recipe: Recipe, val reason: String)

class AIRecommendationRepository(
    private val recipeRepository: RecipeRepository = RecipeRepository()
) {
    private val model = GenerativeModel(
        modelName = "gemini-3.5-flash-lite",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )

    suspend fun getRecommendations(
        user: User,
        candidateRecipes: List<Recipe>,
        limit: Int = 5
    ): Result<List<Recommendation>> {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            Log.e(TAG, "GEMINI_API_KEY is blank — did you add it to local.properties?")
            return Result.failure(IllegalStateException("GEMINI_API_KEY is not set in local.properties"))
        }

        return try {
            val prompt = buildPrompt(user, candidateRecipes, limit)
            Log.d(TAG, "Sending prompt to Gemini:\n$prompt")

            val response = model.generateContent(prompt)
            val json = response.text ?: return Result.failure(IllegalStateException("Empty response from Gemini"))
            Log.d(TAG, "Gemini raw response:\n$json")

            val recommendations = parseRecommendations(json, candidateRecipes)
            Log.d(TAG, "Parsed ${recommendations.size} recommendations (${recommendations.count { it.recipe.rating == 0.0 }} newly invented)")
            Result.success(recommendations)
        } catch (e: Exception) {
            Log.e(TAG, "Gemini recommendation call failed", e)
            Result.failure(e)
        }
    }

    suspend fun generateNewRecipe(user: User, craving: String? = null): Result<Recommendation> {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            Log.e(TAG, "GEMINI_API_KEY is blank — did you add it to local.properties?")
            return Result.failure(IllegalStateException("GEMINI_API_KEY is not set in local.properties"))
        }

        return try {
            val prompt = buildGeneratePrompt(user, craving)
            Log.d(TAG, "Sending generate-new-recipe prompt to Gemini:\n$prompt")

            val response = model.generateContent(prompt)
            val json = response.text ?: return Result.failure(IllegalStateException("Empty response from Gemini"))
            Log.d(TAG, "Gemini raw response:\n$json")

            val entry = JSONObject(json)
            val recipe = buildAndPersistNewRecipe(entry)
                ?: return Result.failure(IllegalStateException("Gemini response was missing a title"))
            val reason = entry.optString("reason", "")

            Log.d(TAG, "Generated and saved new recipe: ${recipe.title} (id=${recipe.id})")
            Result.success(Recommendation(recipe, reason))
        } catch (e: Exception) {
            Log.e(TAG, "Gemini generate-new-recipe call failed", e)
            Result.failure(e)
        }
    }

    private fun buildGeneratePrompt(user: User, craving: String?): String {
        return """
            You are a creative recipe-generation engine for a cooking app called RecipeAI.
            Invent ONE brand new, original recipe for this user. Do not just restate a
            well-known dish verbatim — make it feel tailored to their profile below.

            User profile:
            - Dietary preferences: ${user.dietaryPreferences.ifEmpty { listOf("none specified") }}
            - Cooking skill level: ${user.cookingSkillLevel.ifBlank { "not specified" }}
            - Ingredients on hand: ${user.inventory.ifEmpty { listOf("none specified") }}
            ${if (!craving.isNullOrBlank()) "- They're specifically craving: $craving" else ""}

            Respond with ONLY a raw JSON object (no markdown fences, no explanation) in exactly this shape:
            { "title": "...", "description": "...", "ingredients": ["..."], "instructions": ["..."], "cookingTime": <minutes as int>, "difficulty": "Easy|Intermediate|Advanced", "cuisine": "...", "mealType": "Breakfast|Lunch|Dinner|Snack", "dietaryRestrictions": ["..."], "servings": <int>, "reason": "<one short sentence on why you created this for them>" }
        """.trimIndent()
    }

    private companion object {
        const val TAG = "AIRecommendation"
        // Gemini's own image models (Nano Banana) and Imagen 4 are both unreachable on
        // this project's free API key (0 quota / "no longer available to new users"),
        // so image generation is routed through Hugging Face's free credits instead.
        const val IMAGE_MODEL = "stabilityai/stable-diffusion-3-medium-diffusers"
        const val MAX_IMAGE_DIMENSION_PX = 512
        const val IMAGE_JPEG_QUALITY = 60
    }

    private fun buildPrompt(user: User, candidateRecipes: List<Recipe>, limit: Int): String {
        val candidatesJson = JSONArray().apply {
            candidateRecipes.forEach { recipe ->
                put(
                    JSONObject().apply {
                        put("id", recipe.id)
                        put("title", recipe.title)
                        put("cuisine", recipe.cuisine)
                        put("difficulty", recipe.difficulty)
                        put("cookingTime", recipe.cookingTime)
                        put("dietaryRestrictions", JSONArray(recipe.dietaryRestrictions))
                        put("ingredients", JSONArray(recipe.ingredients))
                    }
                )
            }
        }

        return """
            You are a recipe recommendation engine for a cooking app called RecipeAI.
            Recommend up to $limit recipes for this user, today.

            User profile:
            - Dietary preferences: ${user.dietaryPreferences.ifEmpty { listOf("none specified") }}
            - Cooking skill level: ${user.cookingSkillLevel.ifBlank { "not specified" }}
            - Ingredients on hand: ${user.inventory.ifEmpty { listOf("none specified") }}
            - Recently viewed recipes: ${user.viewHistory.ifEmpty { listOf("none yet") }}

            Recipes already in the app's database (JSON array), reference these by exact "id" when they fit well:
            $candidatesJson

            For each recommendation, either:
            (a) reference an existing recipe above by its exact "id", or
            (b) invent a brand new recipe if nothing above fits the user well, especially using ingredients they already have on hand.

            Respond with ONLY a raw JSON array (no markdown fences, no explanation) in exactly this shape:
            [
              { "type": "existing", "id": "<exact id from the list above>", "reason": "<one short sentence why this fits this user>" },
              { "type": "new", "title": "...", "description": "...", "ingredients": ["..."], "instructions": ["..."], "cookingTime": <minutes as int>, "difficulty": "Easy|Intermediate|Advanced", "cuisine": "...", "mealType": "Breakfast|Lunch|Dinner|Snack", "dietaryRestrictions": ["..."], "servings": <int>, "reason": "<one short sentence why this fits this user>" }
            ]
        """.trimIndent()
    }

    private suspend fun parseRecommendations(json: String, candidateRecipes: List<Recipe>): List<Recommendation> {
        val byId = candidateRecipes.associateBy { it.id }
        val array = JSONArray(json)
        val results = mutableListOf<Recommendation>()

        for (i in 0 until array.length()) {
            val entry = array.getJSONObject(i)
            val reason = entry.optString("reason", "")

            when (entry.optString("type")) {
                "existing" -> {
                    val recipe = byId[entry.optString("id")]
                    if (recipe != null) {
                        results.add(Recommendation(recipe, reason))
                    }
                }
                "new" -> {
                    val recipe = buildAndPersistNewRecipe(entry)
                    if (recipe != null) {
                        results.add(Recommendation(recipe, reason))
                    }
                }
            }
        }

        return results
    }

    private suspend fun buildAndPersistNewRecipe(entry: JSONObject): Recipe? {
        val title = entry.optString("title").ifBlank { return null }
        val cuisine = entry.optString("cuisine")
        val ingredients = entry.optJSONArray("ingredients").toStringList()
        val imageUrl = fetchImageUrl(title, cuisine, ingredients) ?: ""

        val recipe = Recipe(
            title = title,
            description = entry.optString("description"),
            imageUrl = imageUrl,
            ingredients = entry.optJSONArray("ingredients").toStringList(),
            instructions = entry.optJSONArray("instructions").toStringList(),
            cookingTime = entry.optInt("cookingTime", 30),
            difficulty = entry.optString("difficulty", "Easy"),
            cuisine = entry.optString("cuisine"),
            rating = 0.0,
            reviewCount = 0,
            mealType = entry.optString("mealType"),
            dietaryRestrictions = entry.optJSONArray("dietaryRestrictions").toStringList(),
            servings = entry.optInt("servings", 2)
        )

        val addResult = recipeRepository.addRecipeAndReturnId(recipe)
        return addResult.getOrNull()?.let { newId -> recipe.copy(id = newId) }
    }

    private fun JSONArray?.toStringList(): List<String> {
        if (this == null) return emptyList()
        return (0 until length()).map { getString(it) }
    }

    /**
     * Prefers a custom Gemini-generated photo of the dish; falls back to Wikimedia
     * Commons search (and eventually no image) if generation fails for any reason
     * (quota, blank API key, network, etc.) so recipe creation never hard-fails.
     */
    private suspend fun fetchImageUrl(title: String, cuisine: String, ingredients: List<String>): String? {
        generateAndUploadImage(title, cuisine, ingredients)?.let { return it }
        return fetchWikimediaImageUrl(title, cuisine, ingredients)
    }

    private suspend fun generateAndUploadImage(title: String, cuisine: String, ingredients: List<String>): String? {
        val bytes = generateRecipeImageBytes(title, cuisine, ingredients) ?: return null
        return encodeAsDataUri(bytes)
    }

    private suspend fun generateRecipeImageBytes(title: String, cuisine: String, ingredients: List<String>): ByteArray? =
        withContext(Dispatchers.IO) {
            if (BuildConfig.HF_API_KEY.isBlank()) {
                Log.w(TAG, "HF_API_KEY is blank — did you add it to local.properties?")
                return@withContext null
            }

            try {
                val prompt = """
                    A professional, appetizing food-photography shot of "$title"${if (cuisine.isNotBlank()) " ($cuisine cuisine)" else ""}.
                    Key ingredients: ${ingredients.take(6).joinToString(", ").ifBlank { "as described" }}.
                    Overhead or 45-degree angle, natural lighting, on a simple plate or bowl, no text or watermarks.
                """.trimIndent()

                val requestBody = JSONObject().put("inputs", prompt)

                val url = URL("https://router.huggingface.co/hf-inference/models/$IMAGE_MODEL")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.connectTimeout = 30000
                connection.readTimeout = 30000
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization", "Bearer ${BuildConfig.HF_API_KEY}")
                connection.outputStream.use { it.write(requestBody.toString().toByteArray()) }

                val responseCode = connection.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.w(TAG, "Hugging Face image generation for \"$title\" returned HTTP $responseCode: $errorBody")
                    return@withContext null
                }

                connection.inputStream.use { it.readBytes() }
            } catch (e: Exception) {
                Log.w(TAG, "Hugging Face image generation for \"$title\" failed", e)
                null
            }
        }

    /**
     * Firestore caps documents at 1MB, so the raw Gemini image (often 1MB+ on its own)
     * gets downscaled and re-compressed before being base64-encoded into imageUrl as a
     * data: URI - no external hosting or Firebase Storage (Blaze plan) needed.
     */
    private fun encodeAsDataUri(bytes: ByteArray): String? = try {
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
        val scale = MAX_IMAGE_DIMENSION_PX.toFloat() / maxOf(bitmap.width, bitmap.height)
        val resized = if (scale < 1f) {
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
        } else {
            bitmap
        }

        val output = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, IMAGE_JPEG_QUALITY, output)
        val encoded = Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
        "data:image/jpeg;base64,$encoded"
    } catch (e: Exception) {
        Log.w(TAG, "Encoding generated recipe image failed", e)
        null
    }

    /**
     * Tries progressively more generic searches so a fanciful AI-invented dish name
     * (e.g. "Sunrise Citrus Quinoa Bowl") still has a good chance of finding a real,
     * relevant photo even if the exact phrase has no match on Wikimedia Commons.
     */
    private suspend fun fetchWikimediaImageUrl(title: String, cuisine: String, ingredients: List<String>): String? {
        val mainIngredient = ingredients.firstOrNull().orEmpty()
        val candidateQueries = listOfNotNull(
            title,
            if (cuisine.isNotBlank() && mainIngredient.isNotBlank()) "$cuisine $mainIngredient" else null,
            mainIngredient.ifBlank { null },
            cuisine.ifBlank { null }?.let { "$it food" }
        ).distinct()

        for (query in candidateQueries) {
            val result = searchWikimediaImage(query)
            if (result != null) {
                Log.d(TAG, "Image match for \"$title\" found via query \"$query\"")
                return result
            }
        }
        Log.w(TAG, "No Wikimedia image match for \"$title\" after trying: $candidateQueries")
        return null
    }

    private suspend fun searchWikimediaImage(query: String): String? = withContext(Dispatchers.IO) {
        try {
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = URL(
                "https://commons.wikimedia.org/w/api.php?action=query&format=json&prop=imageinfo" +
                        "&generator=search&gsrnamespace=6&gsrsearch=$encodedQuery&gsrlimit=1&iiprop=url&iiurlwidth=800"
            )
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            // Wikimedia's API policy throttles/rejects requests with no descriptive User-Agent.
            connection.setRequestProperty("User-Agent", "RecipeAI-Android/1.0 (college project; contact: ${BuildConfig.APPLICATION_ID})")

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.w(TAG, "Wikimedia image search for \"$query\" returned HTTP $responseCode")
                return@withContext null
            }

            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val pages = JSONObject(body).optJSONObject("query")?.optJSONObject("pages") ?: return@withContext null
            val firstPage = pages.keys().asSequence().firstOrNull()?.let { pages.getJSONObject(it) } ?: return@withContext null
            val imageInfo = firstPage.optJSONArray("imageinfo")?.optJSONObject(0) ?: return@withContext null

            val thumbUrl = imageInfo.optString("thumburl")
            val fullUrl = imageInfo.optString("url")
            when {
                thumbUrl.isNotBlank() -> thumbUrl
                fullUrl.isNotBlank() -> fullUrl
                else -> null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Wikimedia image search for \"$query\" failed", e)
            null
        }
    }
}