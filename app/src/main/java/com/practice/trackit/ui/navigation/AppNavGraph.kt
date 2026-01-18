package com.practice.trackit.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.practice.trackit.ui.auth.AuthViewModel
import com.practice.trackit.ui.auth.LoginScreen
import com.practice.trackit.ui.auth.SignupScreen
import com.practice.trackit.ui.auth.SplashScreen
import com.practice.trackit.ui.dashboard.DashboardScreen
import com.practice.trackit.ui.expense.AddExpenseScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH, builder ={


        composable(AppRoutes.SPLASH) {
            val auth = FirebaseAuth.getInstance()

            LaunchedEffect(Unit) {
                delay(1200) // optional smooth UX

                if (auth.currentUser != null) {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            }

            SplashScreen()
        }


        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                navController = navController,
                onSignUpClick = {
                    navController.navigate(AppRoutes.SIGNUP)
                }
            )
        }



        composable(route = AppRoutes.SIGNUP) {
            SignupScreen(
                navController = navController,
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }



        composable(route = AppRoutes.DASHBOARD) {

            // ✅ Declare ViewModel FIRST
            val authViewModel: AuthViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel()

            // ✅ Call DashboardScreen ONCE
            DashboardScreen(
                navController = navController,
                authViewModel =  authViewModel,

                // ADD EXPENSE
                onAddExpenseClick = {
                    navController.navigate(
                        "${AppRoutes.ADD_TRANSACTION}?type=EXPENSE"
                    )
                },

                // ADD INCOME
                onAddIncomeClick = {
                    navController.navigate(
                        "${AppRoutes.ADD_TRANSACTION}?type=INCOME"
                    )
                },

                // EDIT TRANSACTION
                onTransactionClick = { transaction ->
                    navController.navigate(
                        "${AppRoutes.ADD_TRANSACTION}?type=${transaction.type.name}&expenseId=${transaction.id}"
                    )
                }
            )
        }



        composable(
            route = "${AppRoutes.ADD_TRANSACTION}?type={type}&expenseId={expenseId}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                    defaultValue = "EXPENSE"
                },
                navArgument("expenseId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->

            val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
            val expenseId = backStackEntry.arguments?.getString("expenseId")

            AddExpenseScreen(
                type = type,
                expenseId = expenseId,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }




    })
}