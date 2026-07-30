package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.ui.components.BottomNavBar
import week11.st482988.recipeai_finalproject.ui.components.NavTab
import week11.st482988.recipeai_finalproject.ui.components.RecipeCard
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.MutedText
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = MutedText)
            Text(text = "Search recipes, ingredients ...", style = MaterialTheme.typography.bodyMedium, color = MutedText)
        }

        Text(
            text = "Recommended for you",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recommendedRecipes) { recipe ->
                RecipeCard(recipe = recipe, onClick = { }, onFavoriteClick = { })
            }
        }

        BottomNavBar(
            selectedTab = NavTab.HOME,
            onHomeClick = { },
            onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
            onInventoryClick = { },
            onProfileClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}

private val recommendedRecipes = listOf(
    Recipe(id = "1", title = "Pumpkin Soup", imageUrl = "", cookingTime = 15, difficulty = "Food", rating = 4.5, reviewCount = 245),
    Recipe(id = "2", title = "Pancake", imageUrl = "", cookingTime = 20, difficulty = "Food", rating = 4.8, reviewCount = 312),
    Recipe(id = "3", title = "Greek Salad", imageUrl = "", cookingTime = 65, difficulty = "Food", rating = 4.3, reviewCount = 98),
    Recipe(id = "4", title = "Grilled Chicken", imageUrl = "", cookingTime = 30, difficulty = "Food", rating = 4.6, reviewCount = 150)
)
