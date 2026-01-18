package com.practice.trackit.ui.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.practice.trackit.R
import com.practice.trackit.component.GoogleButton
import com.practice.trackit.ui.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavController,
    onLoginClick: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }


    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    // 🔹 Handle Auth State
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(AppRoutes.DASHBOARD) {
                    popUpTo(AppRoutes.SIGNUP) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    // 🔹 Google Sign-In launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let {
                    viewModel.loginWithGoogle(it)
                }
            } catch (e: ApiException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 🔹 Init Google Client
    LaunchedEffect(Unit) {
        val webClientId = context.getString(R.string.default_web_client_id)
        viewModel.initGoogleSignIn(context, webClientId)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.trackit_logo),
            contentDescription = "TrackIt Logo",
            modifier = Modifier.size(124.dp),
        )

        // Welcome Text
        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        Text(
            text = "Start tracking your expenses",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Email",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = "Enter your email",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = Color(0xFF0A6624),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Password Field
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = "Create a password",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = Color(0xFF0A6624),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Confirm Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = confirmpassword,
                onValueChange = { confirmpassword = it },
                placeholder = {
                    Text(
                        text = "Re-enter your password",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = Color(0xFF0A6624),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))


        // Register Button
        Button(
            onClick = {
                if (password != confirmpassword) {
                    Toast.makeText(
                        context,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.signup(email, password)
                }
            },

            enabled = authState !is AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A6624)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(
                    text = "Register",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }





        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f))
            Text("Or Continue with", modifier = Modifier.padding(horizontal = 12.dp))
            Divider(modifier = Modifier.weight(1f))
        }


        Spacer(modifier = Modifier.height(18.dp))
        GoogleButton {
            val client = viewModel.googleSignInClient
            if (client == null) {
                Toast.makeText(
                    context,
                    "Google Sign-In not ready yet",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                launcher.launch(client.signInIntent)
            }
        }



        Spacer(modifier = Modifier.height(18.dp))

        // Sign Up Link
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            TextButton(
                onClick = onLoginClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0A6624)
                )
            }
        }
    }
}







