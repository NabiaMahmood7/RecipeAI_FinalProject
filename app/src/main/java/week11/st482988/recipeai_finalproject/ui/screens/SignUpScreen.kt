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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st482988.recipeai_finalproject.ui.components.AuthTextField
import week11.st482988.recipeai_finalproject.ui.components.CustomButton
import week11.st482988.recipeai_finalproject.ui.navigation.Screen
import week11.st482988.recipeai_finalproject.ui.theme.Primary
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthUiState
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }

    val uiState = authViewModel.uiState.collectAsState().value
    val isLoading = authViewModel.isLoading.collectAsState().value
    val currentUser = authViewModel.currentUser.collectAsState().value

    if (currentUser != null && uiState is AuthUiState.Success) {
        navController.navigate(Screen.ProfileSetup.route) {
            popUpTo(Screen.SignUp.route) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Create your account",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Get recipes made for your kitchen",
                style = MaterialTheme.typography.titleLarge,
                color = SubtitleGray
            )

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Full name", style = MaterialTheme.typography.titleMedium)
                AuthTextField(value = fullName, onValueChange = { fullName = it }, label = "Alex", modifier = Modifier.fillMaxWidth())
            }

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Email", style = MaterialTheme.typography.titleMedium)
                AuthTextField(value = email, onValueChange = { email = it }, label = "you@example.com", keyboardType = KeyboardType.Email, modifier = Modifier.fillMaxWidth())
            }

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Password", style = MaterialTheme.typography.titleMedium)
                AuthTextField(value = password, onValueChange = { password = it }, label = "Min 8 chars", isPassword = true, modifier = Modifier.fillMaxWidth())
            }

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Confirm Password", style = MaterialTheme.typography.titleMedium)
                AuthTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth(),
                    errorMessage = if (password != confirmPassword && confirmPassword.isNotEmpty()) "Passwords don't match" else null
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(checked = agreedToTerms, onCheckedChange = { agreedToTerms = it }, colors = CheckboxDefaults.colors(checkedColor = Primary))
                Text(text = "I agree to Terms", style = MaterialTheme.typography.labelSmall, color = SubtitleGray, modifier = Modifier.padding(start = 8.dp))
            }

            if (uiState is AuthUiState.Error) {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            CustomButton(
                text = "Create account",
                onClick = { if (agreedToTerms && password == confirmPassword) authViewModel.register(email, password, fullName) },
                isLoading = isLoading,
                enabled = agreedToTerms && password == confirmPassword && fullName.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Already have an account? ", style = MaterialTheme.typography.bodyMedium, color = SubtitleGray)
            TextButton(onClick = { navController.navigate(Screen.Login.route) }, contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
                Text(text = "Log in", style = MaterialTheme.typography.bodyMedium, color = Primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
