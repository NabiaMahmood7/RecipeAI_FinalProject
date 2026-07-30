package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.BottomNavBar
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
import week11.st482988.recipeai_finalproject.ui.components.NavTab
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.ChipGreen
import week11.st482988.recipeai_finalproject.ui.theme.LogoutRed
import week11.st482988.recipeai_finalproject.ui.theme.Primary
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val currentUser = authViewModel.currentUser.collectAsState().value

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f).padding(24.dp)) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(86.dp)
                        .background(Primary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Avatar",
                        tint = Primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
                Column {
                    Text(text = currentUser?.fullName ?: "Alex", style = MaterialTheme.typography.headlineLarge)
                    Text(
                        text = currentUser?.email ?: "you@example.com",
                        style = MaterialTheme.typography.titleLarge,
                        color = SubtitleGray
                    )
                }
            }

            Text(
                text = "Dietary Preferences",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val preferences = currentUser?.dietaryPreferences?.takeIf { it.isNotEmpty() }
                    ?: listOf("Vegetarian", "Vegan", "Gluten-free", "Dairy-free")
                preferences.forEach { preference ->
                    DietaryChip(text = preference)
                }
                DietaryChip(text = "+")
            }
        }

        CustomButton(
            text = "Log out",
            onClick = {
                authViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Profile.route) { inclusive = true }
                }
            },
            backgroundColor = LogoutRed,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        BottomNavBar(
            selectedTab = NavTab.PROFILE,
            onHomeClick = { navController.navigate(Screen.Home.route) },
            onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
            onInventoryClick = { },
            onProfileClick = { }
        )
    }
}

@Composable
private fun DietaryChip(text: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .background(ChipGreen, RoundedCornerShape(21.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (text == "+") {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add preference", tint = Color.White, modifier = Modifier.size(16.dp))
        } else {
            Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color.White)
        }
    }
}
