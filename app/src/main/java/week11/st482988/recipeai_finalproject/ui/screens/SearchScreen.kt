package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.RecipeCard
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.ChipGreen
import week11.st482988.recipeai_finalproject.ui.theme.MutedText
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

private val filterOptions = listOf("Italian", "Asian", "Mexican", "Easy", "Vegetarian", "Under 30 min")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel
) {
    var query by remember { mutableStateOf("") }
    var activeFilters by remember { mutableStateOf(setOf<String>()) }

    val allRecipes = recipeViewModel.recipes.collectAsState().value
    val currentUser = authViewModel.currentUser.collectAsState().value

    val filteredRecipes = allRecipes.filter { recipe ->
        val matchesQuery = query.isBlank() ||
                recipe.title.contains(query, ignoreCase = true) ||
                recipe.ingredients.any { it.contains(query, ignoreCase = true) }

        val matchesFilters = activeFilters.all { filter ->
            when (filter) {
                "Italian", "Asian", "Mexican" -> recipe.cuisine.equals(filter, ignoreCase = true)
                "Easy" -> recipe.difficulty.equals("Easy", ignoreCase = true) ||
                        recipe.difficulty.equals("Beginner", ignoreCase = true)
                "Vegetarian" -> recipe.dietaryRestrictions.any { it.equals("Vegetarian", ignoreCase = true) }
                "Under 30 min" -> recipe.cookingTime in 1..30
                else -> true
            }
        }

        matchesQuery && matchesFilters
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Search recipes",
                style = MaterialTheme.typography.headlineLarge
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
            Box(modifier = Modifier.fillMaxWidth()) {
                if (query.isEmpty()) {
                    Text(text = "Chicken, garlic, rice .....", style = MaterialTheme.typography.bodyMedium, color = MutedText)
                }
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize, color = Color.Black),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { option ->
                val selected = activeFilters.contains(option)
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) ChipGreen else ChipGreen.copy(alpha = 0.4f),
                            RoundedCornerShape(21.dp)
                        )
                        .clickable {
                            activeFilters = if (selected) activeFilters - option else activeFilters + option
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(text = option, style = MaterialTheme.typography.bodySmall, color = Color.White)
                }
            }
        }

        if (filteredRecipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = if (allRecipes.isEmpty()) "No recipes in the database yet." else "No recipes match your search.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleGray
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { id -> navController.navigate(Screen.RecipeDetail.createRoute(id)) },
                        onFavoriteClick = { authViewModel.toggleFavorite(recipe.id) },
                        isFavorite = currentUser?.favorites?.contains(recipe.id) == true
                    )
                }
            }
        }
    }
}