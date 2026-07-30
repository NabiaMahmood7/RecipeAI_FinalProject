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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    var selectedNavItem by remember { mutableStateOf(3) }
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            item { Text(text = "My Profile", style = MaterialTheme.typography.headlineSmall) }
            item {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Name: ${currentUser?.fullName ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Email: ${currentUser?.email ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Level: ${currentUser?.cookingSkillLevel ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            item { CustomButton(text = "Logout", onClick = { authViewModel.logout(); navController.navigate(Screen.Login.route) { popUpTo(Screen.Profile.route) { inclusive = true } } }) }
        }
        NavigationBar(modifier = Modifier.fillMaxWidth().height(56.dp), containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
            NavigationBarItem(icon = { Icon(Icons.Filled.Home, "") }, label = { Text("Home") }, selected = selectedNavItem == 0, onClick = { selectedNavItem = 0; navController.navigate(Screen.Home.route) })
            NavigationBarItem(icon = { Icon(Icons.Filled.Search, "") }, label = { Text("Search") }, selected = selectedNavItem == 1, onClick = { selectedNavItem = 1 })
            NavigationBarItem(icon = { Icon(Icons.Filled.Favorite, "") }, label = { Text("Favorites") }, selected = selectedNavItem == 2, onClick = { selectedNavItem = 2; navController.navigate(Screen.Favorites.route) })
            NavigationBarItem(icon = { Icon(Icons.Filled.Person, "") }, label = { Text("Profile") }, selected = selectedNavItem == 3, onClick = { selectedNavItem = 3 })
        }
    }
}