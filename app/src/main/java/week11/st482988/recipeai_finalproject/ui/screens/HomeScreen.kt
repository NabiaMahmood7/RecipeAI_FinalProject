package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.RecipeCard
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.data.model.Recipe

@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }
    var selectedNavItem by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Hi, ${currentUser?.fullName?.split(" ")?.firstOrNull() ?: "User"}! 👋", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = "What would you like to cook today?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        SearchBar(query = searchQuery, onQueryChange = { searchQuery = it }, onSearch = {}, active = false, onActiveChange = {}, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp), placeholder = { Text("Search...") })

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)) {
            item {
                Text(text = "Recommended", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(5) { index ->
                RecipeCard(recipe = Recipe(id = "$index", title = listOf("Butternut Squash", "Stir Fry", "Greek Salad", "Pasta", "Grilled Chicken")[index], imageUrl = "", cookingTime = 30 + (index * 10), difficulty = listOf("Beginner", "Intermediate", "Advanced")[index % 3], rating = 4.5, reviewCount = 120), onClick = { }, onFavoriteClick = {})
            }
        }

        NavigationBar(modifier = Modifier.fillMaxWidth().height(56.dp), containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
            NavigationBarItem(icon = { Icon(Icons.Filled.Home, "") }, label = { Text("Home") }, selected = selectedNavItem == 0, onClick = { selectedNavItem = 0 })
            NavigationBarItem(icon = { Icon(Icons.Filled.Search, "") }, label = { Text("Search") }, selected = selectedNavItem == 1, onClick = { selectedNavItem = 1 })
            NavigationBarItem(icon = { Icon(Icons.Filled.Favorite, "") }, label = { Text("Favorites") }, selected = selectedNavItem == 2, onClick = { selectedNavItem = 2; navController.navigate(Screen.Favorites.route) })
            NavigationBarItem(icon = { Icon(Icons.Filled.Person, "") }, label = { Text("Profile") }, selected = selectedNavItem == 3, onClick = { selectedNavItem = 3; navController.navigate(Screen.Profile.route) })
        }
    }
}