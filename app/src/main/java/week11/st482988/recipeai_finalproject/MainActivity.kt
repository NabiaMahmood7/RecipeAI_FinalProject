
package week11.st482988.recipeai_finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import week11.st482988.recipeai_finalproject.ui.navigation.SetupNavGraph
import week11.st482988.recipeai_finalproject.ui.theme.RecipeAITheme
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import week11.st482988.recipeai_finalproject.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel = ViewModelProvider(this@MainActivity)[AuthViewModel::class.java]
                    val recipeViewModel = ViewModelProvider(this@MainActivity)[RecipeViewModel::class.java]

                    SetupNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        recipeViewModel = recipeViewModel
                    )
                }
            }
        }
    }
}
