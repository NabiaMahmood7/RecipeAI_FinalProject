
package week11.st482988.recipeai_finalproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.ui.screens.LoginScreen
import week11.st482988.recipeai_finalproject.ui.screens.SignUpScreen
import week11.st482988.recipeai_finalproject.ui.screens.ProfileSetupScreen
import week11.st482988.recipeai_finalproject.ui.screens.HomeScreen
import week11.st482988.recipeai_finalproject.ui.screens.FavoritesScreen
import week11.st482988.recipeai_finalproject.ui.screens.ProfileScreen
import week11.st482988.recipeai_finalproject.ui.screens.SplashScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value

    val startDestination = if (currentUser != null) {
        if (currentUser.cookingSkillLevel.isEmpty()) {
            Screen.ProfileSetup.route
        } else {
            Screen.Home.route
        }
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object Search : Screen("search")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object Inventory : Screen("inventory")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
