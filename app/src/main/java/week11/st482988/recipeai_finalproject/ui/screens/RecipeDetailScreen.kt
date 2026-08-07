package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import week11.st482988.recipeai_finalproject.ui.components.AuthTextField
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
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
import week11.st482988.recipeai_finalproject.data.model.Review
import week11.st482988.recipeai_finalproject.ui.components.RecipeImage
import week11.st482988.recipeai_finalproject.ui.theme.CardMetaGray
import week11.st482988.recipeai_finalproject.ui.theme.Primary
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

private enum class DetailTab { INGREDIENTS, INSTRUCTIONS }

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    navController: NavHostController,
    recipeViewModel: RecipeViewModel,
    authViewModel: AuthViewModel
) {
    LaunchedEffect(recipeId) {
        recipeViewModel.getRecipeById(recipeId)
        recipeViewModel.loadReviews(recipeId)
        authViewModel.recordView(recipeId)
    }

    val recipe = recipeViewModel.selectedRecipe.collectAsState().value
    val isLoading = recipeViewModel.isLoading.collectAsState().value
    val reviews = recipeViewModel.reviews.collectAsState().value
    val isSubmittingReview = recipeViewModel.isSubmittingReview.collectAsState().value
    val currentUser = authViewModel.currentUser.collectAsState().value
    var selectedTab by remember { mutableStateOf(DetailTab.INGREDIENTS) }
    val isFavorite = currentUser?.favorites?.contains(recipeId) == true

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
            return@Column
        }

        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Recipe not found.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtitleGray
                )
            }
            return@Column
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box {
                RecipeImage(
                    imageUrl = recipe.imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier.fillMaxWidth().height(275.dp)
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(42.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }

                IconButton(
                    onClick = { authViewModel.toggleFavorite(recipeId) },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 16.dp, y = 31.dp)
                        .size(62.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Primary
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DetailTabBar(selectedTab = selectedTab, onSelect = { selectedTab = it })

                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = recipe.title.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = Primary, modifier = Modifier.size(14.dp))
                            Text(text = "${recipe.reviewCount}", style = MaterialTheme.typography.bodySmall, color = CardMetaGray)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Filled.Timer, contentDescription = null, tint = CardMetaGray, modifier = Modifier.size(14.dp))
                            Text(text = "${recipe.cookingTime} min.", style = MaterialTheme.typography.bodySmall, color = CardMetaGray)
                        }
                    }
                }

                when (selectedTab) {
                    DetailTab.INGREDIENTS -> IngredientsTab(servings = recipe.servings, ingredients = recipe.ingredients)
                    DetailTab.INSTRUCTIONS -> InstructionsTab(instructions = recipe.instructions)
                }

                ReviewsSection(
                    reviews = reviews,
                    isSubmitting = isSubmittingReview,
                    canReview = currentUser != null,
                    onSubmit = { rating, comment ->
                        val user = currentUser ?: return@ReviewsSection
                        recipeViewModel.submitReview(
                            recipeId = recipeId,
                            userId = user.uid,
                            userName = user.fullName.ifBlank { "Anonymous" },
                            rating = rating,
                            comment = comment
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DetailTabBar(selectedTab: DetailTab, onSelect: (DetailTab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFE3E8EB), RoundedCornerShape(46.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DetailTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .then(
                        if (selected) Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(30.dp))
                        else Modifier
                    )
                    .clickable { onSelect(tab) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (tab == DetailTab.INGREDIENTS) "Ingredients" else "Instructions",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) Color.Black else CardMetaGray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun IngredientsTab(servings: Int, ingredients: List<String>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Servings", style = MaterialTheme.typography.bodyMedium, color = CardMetaGray)
            Text(text = "$servings", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }

        if (ingredients.isEmpty()) {
            Text(
                text = "No ingredients listed yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = SubtitleGray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            ingredients.forEach { ingredient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.size(4.dp).background(Primary, CircleShape))
                    Text(text = ingredient, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun ReviewsSection(
    reviews: List<Review>,
    isSubmitting: Boolean,
    canReview: Boolean,
    onSubmit: (rating: Int, comment: String) -> Unit
) {
    var selectedRating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Ratings & Reviews", style = MaterialTheme.typography.headlineSmall)

        if (canReview) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (star in 1..5) {
                        Icon(
                            imageVector = if (star <= selectedRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "Rate $star",
                            tint = Primary,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { selectedRating = star }
                        )
                    }
                }
                AuthTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = "Share your thoughts about this recipe",
                    modifier = Modifier.fillMaxWidth()
                )
                CustomButton(
                    text = "Submit review",
                    onClick = {
                        onSubmit(selectedRating, comment)
                        selectedRating = 0
                        comment = ""
                    },
                    isLoading = isSubmitting,
                    enabled = selectedRating > 0 && !isSubmitting,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (reviews.isEmpty()) {
            Text(
                text = "No reviews yet, be the first to share your thoughts!",
                style = MaterialTheme.typography.bodyMedium,
                color = SubtitleGray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            reviews.forEach { review -> ReviewRow(review) }
        }
    }
}

@Composable
private fun ReviewRow(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = review.userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < review.rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        if (review.comment.isNotBlank()) {
            Text(text = review.comment, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun InstructionsTab(instructions: List<String>) {
    if (instructions.isEmpty()) {
        Text(
            text = "No instructions added yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = SubtitleGray,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        instructions.forEachIndexed { index, step ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier.size(20.dp).background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${index + 1}", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
                Text(text = step, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}