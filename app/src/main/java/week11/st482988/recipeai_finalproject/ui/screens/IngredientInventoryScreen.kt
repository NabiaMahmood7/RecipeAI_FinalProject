package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.AuthTextField
import week11.st482988.recipeai_finalproject.ui.components.BottomNavBar
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
import week11.st482988.recipeai_finalproject.ui.components.NavTab
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

private data class InventoryItem(val name: String, val qty: String, val unit: String) {
    fun encode() = "$name|$qty|$unit"

    companion object {
        fun decode(raw: String): InventoryItem {
            val parts = raw.split("|")
            return InventoryItem(
                name = parts.getOrElse(0) { raw },
                qty = parts.getOrElse(1) { "" },
                unit = parts.getOrElse(2) { "" }
            )
        }
    }
}

@Composable
fun IngredientInventoryScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    val items = currentUser?.inventory?.map { InventoryItem.decode(it) } ?: emptyList()
    val isGeneratingRecipe = recipeViewModel.isGeneratingRecipe.collectAsState().value
    val generateRecipeError = recipeViewModel.generateRecipeError.collectAsState().value

    var name by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }

    fun persist(updated: List<InventoryItem>) {
        authViewModel.updateInventory(updated.map { it.encode() })
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
            Text(text = "My ingredients", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "${items.size} items on hand",
                style = MaterialTheme.typography.titleLarge,
                color = SubtitleGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (item.qty.isNotBlank() || item.unit.isNotBlank()) {
                            Text(
                                text = "${item.qty} ${item.unit}".trim(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SubtitleGray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        IconButton(
                            onClick = { persist(items - item) },
                            modifier = Modifier.size(28.dp).background(Color(0xFFF08080), CircleShape)
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Add ingredients", style = MaterialTheme.typography.titleMedium, color = week11.st482988.recipeai_finalproject.ui.theme.SplashGreen)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        AuthTextField(value = name, onValueChange = { name = it }, label = "Name", modifier = Modifier.weight(1.4f))
                        AuthTextField(value = qty, onValueChange = { qty = it }, label = "Qty", modifier = Modifier.weight(1f))
                        AuthTextField(value = unit, onValueChange = { unit = it }, label = "Unit", modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                if (name.isNotBlank()) {
                                    persist(items + InventoryItem(name, qty, unit))
                                    name = ""; qty = ""; unit = ""
                                }
                            },
                            modifier = Modifier.size(40.dp).background(week11.st482988.recipeai_finalproject.ui.theme.Primary, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                        }
                    }
                }
            }

            item {
                CustomButton(
                    text = "Find recipes with these →",
                    onClick = { navController.navigate(Screen.Search.route) },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                )
            }

            item {
                Column {
                    CustomButton(
                        text = if (isGeneratingRecipe) "Cooking up something new..." else "✨ Generate an AI recipe from these",
                        onClick = {
                            currentUser?.let { user ->
                                recipeViewModel.generateNewRecipe(user) { newId ->
                                    navController.navigate(Screen.RecipeDetail.createRoute(newId))
                                }
                            }
                        },
                        isLoading = isGeneratingRecipe,
                        enabled = currentUser != null && !isGeneratingRecipe && items.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                    )

                    if (generateRecipeError != null) {
                        Text(
                            text = "Couldn't generate a recipe ($generateRecipeError)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SubtitleGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        BottomNavBar(
            selectedTab = NavTab.INVENTORY,
            onHomeClick = { navController.navigate(Screen.Home.route) },
            onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
            onInventoryClick = { },
            onProfileClick = { navController.navigate(Screen.Profile.route) }
        )
    }
}