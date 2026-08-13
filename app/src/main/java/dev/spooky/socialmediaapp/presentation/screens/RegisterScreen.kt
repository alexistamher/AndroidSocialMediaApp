package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen() {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rPassword by remember { mutableStateOf("") }

    Scaffold { mainPadding ->
        Column(
            Modifier
                .padding(mainPadding)
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        ) {
            Text("Register", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(name, onValueChange = {
                name = it
            }, Modifier.fillMaxWidth(), label = {
                Text("Display name")
            })

            OutlinedTextField(username, onValueChange = {
                username = it
            }, Modifier.fillMaxWidth(), label = {
                Text("Username")
            })

            OutlinedTextField(email, onValueChange = {
                email = it
            }, Modifier.fillMaxWidth(), label = {
                Text("Email")
            })

            OutlinedTextField(password, onValueChange = {
                password = it
            }, Modifier.fillMaxWidth(), label = {
                Text("Password")
            })

            OutlinedTextField(rPassword, onValueChange = {
                rPassword = it
            }, Modifier.fillMaxWidth(), label = {
                Text("Repeat password")
            })

            Button({}, Modifier.fillMaxWidth()) {
                Text(
                    "Login", Modifier.padding(12.dp)
                )
            }

            TextButton({

            }, Modifier.padding(top = 12.dp)) {
                Text("Already have an account? Log in")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    RegisterScreen()
}