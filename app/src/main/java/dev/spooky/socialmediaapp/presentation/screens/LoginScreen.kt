package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold { mainPadding ->
        Column(
            Modifier
                .padding(mainPadding)
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        ) {
            Text("Login", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(email, onValueChange = {
                email = it
            }, Modifier.fillMaxWidth(), label = {
                Text("Email")
            })
            Box(Modifier.fillMaxWidth()) {
                OutlinedTextField(password, onValueChange = {
                    password = it
                }, Modifier.fillMaxWidth(), label = {
                    Text("Password")
                })

                TextButton(
                    {},
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-28).dp),

                    ) {
                    Text("Forgot?")
                }
            }

            Button({}, Modifier.fillMaxWidth()) {
                Text(
                    "Login", Modifier.padding(12.dp)
                )
            }

            TextButton({

            }, Modifier.padding(top = 12.dp)) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScree() {
    LoginScreen()
}
