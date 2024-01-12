package com.app.opticool.presentation.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.opticool.data.model.Login
import com.app.opticool.ui.common.LoginState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    uiState: LoginState,
    onLoginClicked: (Login) -> Unit,
    onLoginSuccess: () -> Unit,
    navigateToSignUp: () -> Unit,
    saveSession: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState is LoginState.Success) {
        saveSession(uiState.user.token)
    } else if (uiState is LoginState.Error) {
        Toast.makeText(LocalContext.current, "Login Gagal", Toast.LENGTH_SHORT).show()
    }

    SignInForm(
        onLoginClicked = onLoginClicked,
        navigateToSignUp = navigateToSignUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInForm(
    onLoginClicked: (Login) -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(36.dp)
        ) {
            Text(
                text = "Welcome Back",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = "Login back into your account",
                fontSize = 16.sp,
                color = Color(0xFF7B7575)
            )
            Spacer(modifier = Modifier.height(70.dp))
            OutlinedTextField(
                value = emailValue,
                onValueChange = { emailValue = it },
                label = { Text(text = "Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = passwordValue,
                onValueChange = { passwordValue = it },
                label = { Text(text = "Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = {
                    onLoginClicked(Login(emailValue, passwordValue))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
            OutlinedButton(
                onClick = navigateToSignUp,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}