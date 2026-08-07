
package week11.st482988.recipeai_finalproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel
import week11.st482988.recipeai_finalproject.ui.screens.LoginScreen
import week11.st482988.recipeai_finalproject.ui.screens.SignUpScreen
import week11.st482988.recipeai_finalproject.ui.screens.ForgotPasswordScreen
import week11.st482988.recipeai_finalproject.ui.screens.ProfileSetupScreen
import week11.st482988.recipeai_finalproject.ui.screens.HomeScreen
import week11.st482988.recipeai_finalproject.ui.screens.FavoritesScreen
import week11.st482988.recipeai_finalproject.ui.screens.ProfileScreen
import week11.st482988.recipeai_finalproject.ui.screens.SplashScreen
import week11.st482988.recipeai_finalproject.ui.screens.SearchScreen
import week11.st482988.recipeai_finalproject.ui.screens.RecipeDetailScreen
import week11.st482988.recipeai_finalproject.ui.screens.IngredientInventoryScreen
import week11.st482988.recipeai_finalproject.ui.screens.HistoryScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                authViewModel = authViewModel
            )
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

        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
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
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                navController = navController,
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }

        composable(route = Screen.Search.route) {
            SearchScreen(
                navController = navController,
                recipeViewModel = recipeViewModel,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.RecipeDetail.route) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            RecipeDetailScreen(
                recipeId = recipeId,
                navController = navController,
                recipeViewModel = recipeViewModel,
                authViewModel = authViewModel
            )
        }

        composable(route = Screen.Inventory.route) {
            IngredientInventoryScreen(
                navController = navController,
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }

        composable(route = Screen.History.route) {
            HistoryScreen(
                navController = navController,
                authViewModel = authViewModel,
                recipeViewModel = recipeViewModel
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object Search : Screen("search")
    object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_detail/$recipeId"
    }
    object Inventory : Screen("inventory")
    object History : Screen("history")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
