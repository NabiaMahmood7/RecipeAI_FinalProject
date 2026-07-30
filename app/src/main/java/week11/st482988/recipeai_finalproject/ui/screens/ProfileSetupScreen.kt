package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.data.repository.AuthRepository
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.currentUser.collectAsState().value
    var selectedPreferences by remember { mutableStateOf(setOf<String>()) }
    var cookingLevel by remember { mutableStateOf("") }

    val preferences = listOf("Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "Keto")
    val skillLevels = listOf("Beginner", "Intermediate", "Advanced")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(text = "Personalize Your Experience", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Dietary Preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            preferences.forEach { preference ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = preference, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        Checkbox(checked = selectedPreferences.contains(preference), onCheckedChange = { isChecked ->
                            selectedPreferences = if (isChecked) selectedPreferences + preference else selectedPreferences - preference
                        }, colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary))
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "Cooking Skill Level", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            skillLevels.forEach { level ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = if (cookingLevel == level) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = level, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        RadioButton(selected = cookingLevel == level, onClick = { cookingLevel = level }, colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary))
                    }
                }
            }
        }

        CustomButton(
            text = "Continue",
            onClick = {
                if (currentUser != null && cookingLevel.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val authRepository = AuthRepository()
                        authRepository.updateUserPreferences(currentUser.uid, selectedPreferences.toList(), cookingLevel)
                        navController.navigate(Screen.Home.route) { popUpTo(Screen.ProfileSetup.route) { inclusive = true } }
                    }
                }
            },
            enabled = cookingLevel.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}