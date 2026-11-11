package app.taplink.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.auth.providers.Email
import kotlinx.coroutines.launch

private val AccountInk = Color(0xFF10131A)
private val AccountMuted = Color(0xFF8D96A8)
private val AccountAccent = Color(0xFF83A7FF)
private val AccountPanel = Color(0xFF1A1F2A)

@Composable
fun AccountScreen() {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignedIn by remember { mutableStateOf(false) }
    var isBusy by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Sign in or create an account to sync your profile.") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Your TapLink account",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Sign in to keep your profile connected across devices.",
            color = AccountMuted
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AccountPanel)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (isSignedIn) {
                    Text("You're signed in", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(email, color = AccountMuted)
                    Button(
                        onClick = {
                            scope.launch {
                                isBusy = true
                                try {
                                    TapLinkBackend.client.auth.signOut()
                                    isSignedIn = false
                                    message = "You've been signed out."
                                    isError = false
                                } catch (error: Exception) {
                                    message = error.message ?: "Sign out failed. Please try again."
                                    isError = true
                                } finally {
                                    isBusy = false
                                }
                            }
                        },
                        enabled = !isBusy,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccountAccent,
                            contentColor = AccountInk
                        )
                    ) {
                        Text(if (isBusy) "Please wait…" else "Sign out")
                    }
                } else {
                    Text("Email", color = Color.White, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("you@example.com") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(14.dp)
                    )
                    Text("Password", color = Color.White, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("At least 8 characters") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Button(
                        onClick = {
                            if (email.isBlank() || password.length < 8) {
                                message = "Enter a valid email and a password with at least 8 characters."
                                isError = true
                            } else {
                                scope.launch {
                                    isBusy = true
                                    try {
                                        TapLinkBackend.client.auth.signInWith(Email) {
                                            this.email = email.trim()
                                            this.password = password
                                        }
                                        isSignedIn = true
                                        message = "Signed in successfully. Your account is ready."
                                        isError = false
                                    } catch (error: Exception) {
                                        message = error.message ?: "Sign in failed. Check your details and try again."
                                        isError = true
                                    } finally {
                                        isBusy = false
                                    }
                                }
                            }
                        },
                        enabled = !isBusy,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccountAccent,
                            contentColor = AccountInk
                        )
                    ) {
                        Text(if (isBusy) "Please wait…" else "Sign in")
                    }

                    OutlinedButton(
                        onClick = {
                            if (email.isBlank() || password.length < 8) {
                                message = "Enter a valid email and a password with at least 8 characters."
                                isError = true
                            } else {
                                scope.launch {
                                    isBusy = true
                                    try {
                                        TapLinkBackend.client.auth.signUpWith(Email) {
                                            this.email = email.trim()
                                            this.password = password
                                        }
                                        message = "Sign-up request sent. If email confirmation is enabled, check your inbox before signing in."
                                        isError = false
                                    } catch (error: Exception) {
                                        message = error.message ?: "Account creation failed. Please try again."
                                        isError = true
                                    } finally {
                                        isBusy = false
                                    }
                                }
                            }
                        },
                        enabled = !isBusy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create account")
                    }
                }
            }
        }

        Text(
            message,
            color = if (isError) Color(0xFFFF9B9B) else AccountMuted,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Your profile data is protected by Supabase row-level security. This screen connects authentication; profile syncing is the next step.",
            color = AccountMuted,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(8.dp))
    }
}
