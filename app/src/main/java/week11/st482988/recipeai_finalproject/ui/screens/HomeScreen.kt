package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.BottomNavBar
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
import week11.st482988.recipeai_finalproject.ui.components.NavTab
import week11.st482988.recipeai_finalproject.ui.components.RecipeCard
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.MutedText
import week11.st482988.recipeai_finalproject.ui.theme.Primary
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    val recipes = recipeViewModel.recipes.collectAsState().value
    val recommendations = recipeViewModel.recommendations.collectAsState().value
    val isLoadingRecommendations = recipeViewModel.isLoadingRecommendations.collectAsState().value
    val recommendationError = recipeViewModel.recommendationError.collectAsState().value
    val isGeneratingRecipe = recipeViewModel.isGeneratingRecipe.collectAsState().value
    val generateRecipeError = recipeViewModel.generateRecipeError.collectAsState().value

    LaunchedEffect(currentUser?.uid) {
        currentUser?.let { recipeViewModel.loadRecommendations(it) }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Good morning", style = MaterialTheme.typography.titleLarge, color = SubtitleGray)
            Text(
                text = currentUser?.fullName?.split(" ")?.firstOrNull() ?: "there",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .border(1.dp, MutedText, RoundedCornerShape(100.dp))
                .clickable { navController.navigate(Screen.Search.route) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = MutedText)
            Text(text = "Search recipes, ingredients ...", style = MaterialTheme.typography.bodyMedium, color = MutedText)
        }

        CustomButton(
            text = if (isGeneratingRecipe) "Cooking up something new..." else "✨ Surprise me with a new AI recipe",
            onClick = {
                currentUser?.let { user ->
                    recipeViewModel.generateNewRecipe(user) { newId ->
                        navController.navigate(Screen.RecipeDetail.createRoute(newId))
                    }
                }
            },
            isLoading = isGeneratingRecipe,
            enabled = currentUser != null && !isGeneratingRecipe,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 12.dp)
        )

        if (generateRecipeError != null) {
            Text(
                text = "Couldn't generate a recipe ($generateRecipeError)",
                style = MaterialTheme.typography.labelSmall,
                color = SubtitleGray,
                modifier = Modifier.padding(horizontal = 24.dp).padding(top = 4.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (recommendations.isNotEmpty()) "Picked for you" else "Recommended for you",
                style = MaterialTheme.typography.headlineSmall
            )
            if (isLoadingRecommendations) {
                CircularProgressIndicator(color = Primary, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            }
        }

        if (recommendationError != null && recommendations.isEmpty()) {
            Text(
                text = "AI recommendations unavailable ($recommendationError) — showing all recipes instead.",
                style = MaterialTheme.typography.labelSmall,
                color = SubtitleGray,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
            )
        }

        when {
            recommendations.isNotEmpty() -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recommendations) { recommendation ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        RecipeCard(
                            recipe = recommendation.recipe,
                            onClick = { id -> navController.navigate(Screen.RecipeDetail.createRoute(id)) },
                            onFavoriteClick = { authViewModel.toggleFavorite(recommendation.recipe.id) },
                            isFavorite = currentUser?.favorites?.contains(recommendation.recipe.id) == true
                        )
                        if (recommendation.reason.isNotBlank()) {
                            Text(
                                text = recommendation.reason,
                                style = MaterialTheme.typography.labelSmall,
                                color = SubtitleGray,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            recipes.isEmpty() -> Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "No recipes yet — check back soon!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleGray
                )
            }

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { id -> navController.navigate(Screen.RecipeDetail.createRoute(id)) },
                        onFavoriteClick = { authViewModel.toggleFavorite(recipe.id) },
                        isFavorite = currentUser?.favorites?.contains(recipe.id) == true
                    )
                }
            }
        }

        BottomNavBar(
            selectedTab = NavTab.HOME,
            onHomeClick = { },
            onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
            onInventoryClick = { navController.navigate(Screen.Inventory.route) },
            onProfileClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}