package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.Primary

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Login.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🍽️",
            fontSize = 80.sp,
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "RecipeAI",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.height(50.dp),
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            text = "Smart Recipe Recommendations",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}