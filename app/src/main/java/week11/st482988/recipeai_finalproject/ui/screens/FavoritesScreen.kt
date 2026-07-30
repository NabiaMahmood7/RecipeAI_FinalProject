package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@Composable
fun FavoritesScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var selectedNavItem by remember { mutableStateOf(2) }
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(text = "Saved Recipes", style = MaterialTheme.typography.headlineSmall)
        }
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item { Text(text = "No saved recipes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(32.dp)) }
        }
        NavigationBar(modifier = Modifier.fillMaxWidth().height(56.dp), containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
            NavigationBarItem(icon = { Icon(Icons.Filled.Home, "") }, label = { Text("Home") }, selected = selectedNavItem == 0, onClick = { selectedNavItem = 0; navController.navigate(Screen.Home.route) })
            NavigationBarItem(icon = { Icon(Icons.Filled.Search, "") }, label = { Text("Search") }, selected = selectedNavItem == 1, onClick = { selectedNavItem = 1 })
            NavigationBarItem(icon = { Icon(Icons.Filled.Favorite, "") }, label = { Text("Favorites") }, selected = selectedNavItem == 2, onClick = { selectedNavItem = 2 })
            NavigationBarItem(icon = { Icon(Icons.Filled.Person, "") }, label = { Text("Profile") }, selected = selectedNavItem == 3, onClick = { selectedNavItem = 3; navController.navigate(Screen.Profile.route) })
        }
    }
}