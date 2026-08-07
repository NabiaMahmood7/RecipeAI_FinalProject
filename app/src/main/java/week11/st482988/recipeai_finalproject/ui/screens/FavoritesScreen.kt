package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import week11.st482988.recipeai_finalproject.ui.components.NavTab
import week11.st482988.recipeai_finalproject.ui.components.RecipeCard
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    val favoriteIds = currentUser?.favorites ?: emptyList()

    LaunchedEffect(favoriteIds) { recipeViewModel.loadFavorites(favoriteIds) }

    val savedRecipes = recipeViewModel.favoriteRecipes.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Favorites", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "${savedRecipes.size} saved recipes",
                style = MaterialTheme.typography.titleLarge,
                color = SubtitleGray
            )
        }

        if (savedRecipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "No saved recipes yet, tap the heart on any recipe to save it here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleGray
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(savedRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { id -> navController.navigate(Screen.RecipeDetail.createRoute(id)) },
                        onFavoriteClick = { authViewModel.toggleFavorite(recipe.id) },
                        isFavorite = true
                    )
                }
            }
        }

        BottomNavBar(
            selectedTab = NavTab.FAVORITES,
            onHomeClick = { navController.navigate(Screen.Home.route) },
            onFavoritesClick = { },
            onInventoryClick = { navController.navigate(Screen.Inventory.route) },
            onProfileClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}