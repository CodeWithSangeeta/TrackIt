package com.practice.trackit.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.trackit.ui.auth.LoginScreen
import com.practice.trackit.ui.auth.SignupScreen
import com.practice.trackit.ui.dashboard.DashboardScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN, builder ={

        //Login Screen
        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    Log.d("NAV", "Navigating to Dashboard")
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(AppRoutes.SIGNUP)
                }
            )
        }


        // Signup Screen
        composable(route = AppRoutes.SIGNUP) {
            SignupScreen(
                onSignupSuccess = {
                    Log.d("NAV", "Navigating to Dashboard")
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.SIGNUP) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }


        // Dashboard Screen
        composable(route = AppRoutes.DASHBOARD) {
            DashboardScreen(
                onTransactionClick = {

                },
                onAddExpenseClick = {
                    // later navigate to AddExpense screen
                }
            )
        }

    })
}