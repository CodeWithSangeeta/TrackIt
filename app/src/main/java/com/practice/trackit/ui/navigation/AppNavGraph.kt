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

        //Login Screen
        composable(route = AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
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


//        // Dashboard Screen
//        composable(route = AppRoutes.DASHBOARD) {
//            // Dashboard
//            DashboardScreen(
//                onAddExpenseClick = {
//                    navController.navigate(AppRoutes.ADD_TRANSACTION)
//                },
//                onTransactionClick = { transaction ->
//                    navController.navigate(
//                        "${AppRoutes.ADD_TRANSACTION}?expenseId=${transaction.id}"
//                    )
//                }
//            )
//
//        }

        composable(route = AppRoutes.DASHBOARD) {
            DashboardScreen(

                // EXPENSE
                onAddExpenseClick = {
                    navController.navigate(
                        "${AppRoutes.ADD_TRANSACTION}?type=EXPENSE"
                    )
                },

                // INCOME ✅
                onAddIncomeClick = {
                    navController.navigate(
                        "${AppRoutes.ADD_TRANSACTION}?type=INCOME"
                    )
                },

                // EDIT
                onTransactionClick = { transaction ->
                    navController.navigate(
                        "${AppRoutes.ADD_TRANSACTION}?type=${transaction.type.name}&expenseId=${transaction.id}"
                    )
                }
            )
        }




//        // Add/Edit screen
//        composable(
//            route = "${AppRoutes.ADD_TRANSACTION}?expenseId={expenseId}",
//            arguments = listOf(
//                navArgument("expenseId") {
//                    type = NavType.StringType
//                    nullable = true
//                    defaultValue = null
//                }
//            )
//        ) { backStackEntry ->
//
//            val expenseId = backStackEntry.arguments?.getString("expenseId")
//
//            AddExpenseScreen(
//                expenseId = expenseId,
//                onBackClick = { navController.popBackStack() },
//                onSaveSuccess = { navController.popBackStack() }
//            )
//        }



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