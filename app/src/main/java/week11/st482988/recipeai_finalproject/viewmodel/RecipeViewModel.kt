package week11.st482988.recipeai_finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.data.repository.RecipeRepository

class RecipeViewModel : ViewModel() {
    private val recipeRepository = RecipeRepository()

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    init {
        loadAllRecipes()
    }

    private fun loadAllRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            recipeRepository.getAllRecipes().collect { recipes ->
                _recipes.value = recipes
                _isLoading.value = false
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