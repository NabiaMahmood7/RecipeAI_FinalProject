package week11.st482988.recipeai_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import week11.st482988.recipeai_finalproject.ui.theme.Primary
import week11.st482988.recipeai_finalproject.ui.theme.SubtitleGray
import week11.st482988.recipeai_finalproject.viewmodel.AuthUiState
import week11.st482988.recipeai_finalproject.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    val uiState = authViewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) { authViewModel.clearError() }

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
                text = "Reset your password",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Enter the email on your account and we'll send a link to reset your password",
                style = MaterialTheme.typography.titleLarge,
                color = SubtitleGray
            )

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Email", style = MaterialTheme.typography.titleMedium)
                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "you@example.com",
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            when (uiState) {
                is AuthUiState.Success -> Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )
                is AuthUiState.Error -> Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
                else -> {}
            }

            CustomButton(
                text = "Send reset link",
                onClick = { authViewModel.resetPassword(email) },
                isLoading = uiState is AuthUiState.Loading,
                enabled = email.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "← Back to Login",
                style = MaterialTheme.typography.bodyMedium,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
