package week11.st482988.recipeai_finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st482988.recipeai_finalproject.data.sampleRecipes
import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.data.model.Review
import week11.st482988.recipeai_finalproject.data.model.User
import week11.st482988.recipeai_finalproject.data.repository.AIRecommendationRepository
import week11.st482988.recipeai_finalproject.data.repository.Recommendation
import week11.st482988.recipeai_finalproject.data.repository.RecipeRepository
import week11.st482988.recipeai_finalproject.data.repository.ReviewRepository

class RecipeViewModel : ViewModel() {
    private val recipeRepository = RecipeRepository()
    private val reviewRepository = ReviewRepository()
    private val aiRecommendationRepository = AIRecommendationRepository(recipeRepository)

    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: StateFlow<List<Recommendation>> = _recommendations.asStateFlow()

    private val _isLoadingRecommendations = MutableStateFlow(false)
    val isLoadingRecommendations: StateFlow<Boolean> = _isLoadingRecommendations.asStateFlow()

    private val _recommendationError = MutableStateFlow<String?>(null)
    val recommendationError: StateFlow<String?> = _recommendationError.asStateFlow()

    fun loadRecommendations(user: User) {
        viewModelScope.launch {
            _isLoadingRecommendations.value = true
            _recommendationError.value = null

            val result = aiRecommendationRepository.getRecommendations(user, _recipes.value)

            result.onSuccess { recs ->
                _recommendations.value = recs
            }
            result.onFailure { error ->
                _recommendationError.value = error.message ?: "Couldn't get AI recommendations"
                _recommendations.value = emptyList()
            }
            _isLoadingRecommendations.value = false
        }
    }

    private val _isGeneratingRecipe = MutableStateFlow(false)
    val isGeneratingRecipe: StateFlow<Boolean> = _isGeneratingRecipe.asStateFlow()

    private val _generateRecipeError = MutableStateFlow<String?>(null)
    val generateRecipeError: StateFlow<String?> = _generateRecipeError.asStateFlow()

    fun generateNewRecipe(user: User, craving: String? = null, onSuccess: (recipeId: String) -> Unit) {
        viewModelScope.launch {
            _isGeneratingRecipe.value = true
            _generateRecipeError.value = null

            val result = aiRecommendationRepository.generateNewRecipe(user, craving)

            result.onSuccess { recommendation -> onSuccess(recommendation.recipe.id) }
            result.onFailure { error -> _generateRecipeError.value = error.message ?: "Couldn't generate a recipe" }

            _isGeneratingRecipe.value = false
        }
    }

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _isSubmittingReview = MutableStateFlow(false)
    val isSubmittingReview: StateFlow<Boolean> = _isSubmittingReview.asStateFlow()

    private val _isSeeding = MutableStateFlow(false)
    val isSeeding: StateFlow<Boolean> = _isSeeding.asStateFlow()

    fun seedSampleRecipes() {
        viewModelScope.launch {
            _isSeeding.value = true
            sampleRecipes.forEach { recipe -> recipeRepository.addRecipe(recipe) }
            _isSeeding.value = false
        }
    }

    init {
        loadAllRecipes()
    }

    private val _historyRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val historyRecipes: StateFlow<List<Recipe>> = _historyRecipes.asStateFlow()

    fun loadFavorites(favoriteIds: List<String>) {
        viewModelScope.launch {
            recipeRepository.getRecipesByIds(favoriteIds).onSuccess { recipes ->
                _favoriteRecipes.value = recipes
            }
        }
    }

    fun loadHistory(historyIds: List<String>) {
        viewModelScope.launch {
            recipeRepository.getRecipesByIds(historyIds).onSuccess { recipes ->
                val order = historyIds.withIndex().associate { (index, id) -> id to index }
                _historyRecipes.value = recipes.sortedBy { order[it.id] ?: Int.MAX_VALUE }
            }
        }
    }

    fun loadReviews(recipeId: String) {
        viewModelScope.launch {
            reviewRepository.getReviews(recipeId).collect { reviews ->
                _reviews.value = reviews
            }
        }
    }

    fun submitReview(recipeId: String, userId: String, userName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            _isSubmittingReview.value = true
            val review = Review(userId = userId, userName = userName, rating = rating, comment = comment)
            reviewRepository.addReview(recipeId, review)
            getRecipeById(recipeId)
            _isSubmittingReview.value = false
        }
    }

    private var hasAutoSeeded = false

    private fun loadAllRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            recipeRepository.getAllRecipes().collect { recipes ->
                _recipes.value = recipes
                _isLoading.value = false

                if (recipes.isEmpty() && !hasAutoSeeded) {
                    hasAutoSeeded = true
                    seedSampleRecipes()
                }
            }
        }
    }

    fun getRecipeById(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = recipeRepository.getRecipeById(recipeId)

            result.onSuccess { recipe ->
                _selectedRecipe.value = recipe
                _isLoading.value = false
            }

            result.onFailure { error ->
                _error.value = error.message ?: "Failed to load recipe"
                _isLoading.value = false
            }
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = recipeRepository.searchRecipes(query)

            result.onSuccess { recipes ->
                _recipes.value = recipes
                _isLoading.value = false
            }

            result.onFailure { error ->
                _error.value = error.message ?: "Search failed"
                _isLoading.value = false
            }
        }
    }

    fun filterByDifficulty(difficulty: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = recipeRepository.getRecipesByDifficulty(difficulty)

            result.onSuccess { recipes ->
                _recipes.value = recipes
                _isLoading.value = false
            }

            result.onFailure { error ->
                _error.value = error.message ?: "Filter failed"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}