package week11.st482988.recipeai_finalproject.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st482988.recipeai_finalproject.ui.theme.MutedText
import week11.st482988.recipeai_finalproject.ui.theme.Primary

enum class NavTab { HOME, FAVORITES, INVENTORY, PROFILE }

@Composable
fun BottomNavBar(
    selectedTab: NavTab,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onInventoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(BorderStroke(1.dp, MutedText))
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onHomeClick) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home",
                tint = if (selectedTab == NavTab.HOME) Primary else MutedText
            )
        }
        IconButton(onClick = onFavoritesClick) {
            Icon(
                imageVector = if (selectedTab == NavTab.FAVORITES) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorites",
                tint = if (selectedTab == NavTab.FAVORITES) Primary else MutedText
            )
        }
        IconButton(onClick = onInventoryClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = "Ingredients",
                tint = if (selectedTab == NavTab.INVENTORY) Primary else MutedText
            )
        }
        IconButton(onClick = onProfileClick) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = if (selectedTab == NavTab.PROFILE) Primary else MutedText
            )
        }
    }
}
