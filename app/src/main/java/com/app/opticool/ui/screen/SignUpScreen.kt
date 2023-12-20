package com.app.opticool.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.opticool.data.model.Register
import com.app.opticool.ui.common.LoginState
import com.app.opticool.ui.common.RegisterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    uiState: RegisterState,
    navigateToSigIn: () -> Unit,
    onRegisterClicked: (Register) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState is RegisterState.Success) {
        Toast.makeText(LocalContext.current, "Login berhasil kamu bisa kembali ke halaman login", Toast.LENGTH_SHORT).show()
    } else if (uiState is RegisterState.Error) {
        Toast.makeText(LocalContext.current, "Login Gagal", Toast.LENGTH_SHORT).show()
    }

    SignUpForm(
        onRegisterClicked = onRegisterClicked,
        navigateToSigIn = navigateToSigIn
    )
}

@Composable
fun SignUpForm(
    navigateToSigIn: () -> Unit,
    onRegisterClicked: (Register) -> Unit,
    modifier: Modifier = Modifier
) {
    var fullNameValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var phoneValue by remember { mutableStateOf("") }
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
                text = "Welcome",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = "Signup into your account",
                fontSize = 16.sp,
                color = Color(0xFF7B7575)
            )
            Spacer(modifier = Modifier.height(70.dp))
            OutlinedTextField(
                value = fullNameValue,
                onValueChange = { fullNameValue = it },
                label = { Text(text = "Full Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = emailValue,
                onValueChange = { emailValue = it },
                label = { Text(text = "Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phoneValue,
                onValueChange = { phoneValue = it },
                label = { Text(text = "Phone Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = passwordValue,
                onValueChange = { passwordValue = it },
                label = { Text(text = "Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = { onRegisterClicked(
                    Register(
                        fullName = fullNameValue,
                        email = emailValue,
                        phoneNumber = phoneValue,
                        password = passwordValue
                    )
                )},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }
            OutlinedButton(
                onClick = { navigateToSigIn() },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
        }
    }
}