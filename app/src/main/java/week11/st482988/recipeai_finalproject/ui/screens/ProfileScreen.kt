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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    val recipes = recipeViewModel.recipes.collectAsState().value
    val isSeeding = recipeViewModel.isSeeding.collectAsState().value
    val isSavingPreferences = authViewModel.isLoading.collectAsState().value

    val dietaryOptions = listOf("Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "Keto")
    val skillLevels = listOf("Beginner", "Intermediate", "Advanced")

    var isEditingPreferences by remember { mutableStateOf(false) }
    var editedDietary by remember { mutableStateOf(setOf<String>()) }
    var editedLevel by remember { mutableStateOf("") }

    LaunchedEffect(currentUser?.uid, isEditingPreferences) {
        if (isEditingPreferences) {
            editedDietary = currentUser?.dietaryPreferences?.toSet() ?: emptySet()
            editedLevel = currentUser?.cookingSkillLevel ?: ""
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()).padding(24.dp)) {
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

            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Preferences", style = MaterialTheme.typography.headlineSmall)
                if (!isEditingPreferences) {
                    IconButton(onClick = { isEditingPreferences = true }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit preferences", tint = Primary)
                    }
                }
            }

            if (isEditingPreferences) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Dietary Preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    dietaryOptions.forEach { preference ->
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            androidx.compose.foundation.layout.Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = preference, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                Checkbox(
                                    checked = editedDietary.contains(preference),
                                    onCheckedChange = { isChecked ->
                                        editedDietary = if (isChecked) editedDietary + preference else editedDietary - preference
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = Primary)
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Cooking Skill Level", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    skillLevels.forEach { level ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (editedLevel == level) Primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            androidx.compose.foundation.layout.Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = level, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                RadioButton(
                                    selected = editedLevel == level,
                                    onClick = { editedLevel = level },
                                    colors = RadioButtonDefaults.colors(selectedColor = Primary)
                                )
                            }
                        }
                    }
                }

                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(onClick = { isEditingPreferences = false }) {
                        Text(text = "Cancel", style = MaterialTheme.typography.bodyMedium, color = SubtitleGray)
                    }
                    CustomButton(
                        text = "Save",
                        onClick = {
                            authViewModel.completeProfileSetup(editedDietary.toList(), editedLevel) {
                                isEditingPreferences = false
                            }
                        },
                        isLoading = isSavingPreferences,
                        enabled = editedLevel.isNotEmpty() && !isSavingPreferences,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Text(text = "Dietary Preferences", style = MaterialTheme.typography.titleMedium, color = SubtitleGray)
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val preferences = currentUser?.dietaryPreferences?.takeIf { it.isNotEmpty() } ?: listOf("None set")
                    preferences.forEach { preference ->
                        DietaryChip(text = preference)
                    }
                }

                Text(
                    text = "Cooking Skill Level",
                    style = MaterialTheme.typography.titleMedium,
                    color = SubtitleGray,
                    modifier = Modifier.padding(top = 20.dp)
                )
                DietaryChip(text = currentUser?.cookingSkillLevel?.ifBlank { "Not set" } ?: "Not set")
            }

            TextButton(
                onClick = { navController.navigate(Screen.History.route) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Recently viewed", style = MaterialTheme.typography.bodyMedium, color = Primary)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.padding(start = 4.dp).size(16.dp)
                )
            }

            if (recipes.isEmpty()) {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    Text(
                        text = "Dev tools",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubtitleGray
                    )
                    CustomButton(
                        text = "Seed sample recipes",
                        onClick = { recipeViewModel.seedSampleRecipes() },
                        isLoading = isSeeding,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
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
            onInventoryClick = { navController.navigate(Screen.Inventory.route) },
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