package week11.st482988.recipeai_finalproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import week11.st482988.recipeai_finalproject.data.model.Recipe
import week11.st482988.recipeai_finalproject.ui.theme.CardMetaGray
import week11.st482988.recipeai_finalproject.ui.theme.CardSecondaryText
import week11.st482988.recipeai_finalproject.ui.theme.CardTitleBlue

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: (String) -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(recipe.id) }
    ) {
        Box {
            RecipeImage(
                imageUrl = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(151.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(31.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) week11.st482988.recipeai_finalproject.ui.theme.Primary else Color(0xFFCCCCCC),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = CardTitleBlue,
            maxLines = 1,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = recipe.difficulty,
                style = MaterialTheme.typography.labelSmall,
                color = CardSecondaryText
            )
            Text(text = "•", style = MaterialTheme.typography.labelSmall, color = CardMetaGray)
            Text(
                text = "${recipe.cookingTime} mins",
                style = MaterialTheme.typography.labelSmall,
                color = CardSecondaryText
            )
        }
    }
}