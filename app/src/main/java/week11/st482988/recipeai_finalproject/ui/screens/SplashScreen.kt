package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.R
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.SplashGreen
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashGreen)
            .padding(horizontal = 32.dp, vertical = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_recipe_ai_logo),
            contentDescription = "RecipeAI logo",
            modifier = Modifier
                .size(168.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Text(
            text = "RecipeAI",
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = "Cook smarter, waste less.",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedButton(
            onClick = {
                val destination = when {
                    currentUser == null -> Screen.Login.route
                    currentUser.cookingSkillLevel.isEmpty() -> Screen.ProfileSetup.route
                    else -> Screen.Home.route
                }
                navController.navigate(destination) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(top = 48.dp)
        ) {
            Text(text = "Continue →")
        }
    }
}
