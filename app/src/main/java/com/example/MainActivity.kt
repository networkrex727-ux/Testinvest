package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    val authViewModel: AuthViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val mineViewModel: MineViewModel = viewModel()
    val blogViewModel: BlogViewModel = viewModel()
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                viewModel = authViewModel,
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("login") { saveState = true }
                    }
                },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "register?ref={ref}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://prime-khatab.web.app/register?ref={ref}"
                },
                navDeepLink {
                    uriPattern = "https://prime-khatab.firebaseapp.com/register?ref={ref}"
                }
            ),
            arguments = listOf(
                navArgument("ref") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val refVal = backStackEntry.arguments?.getString("ref")
            RegisterScreen(
                viewModel = authViewModel,
                initialReferralCode = refVal,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register?ref={ref}") { saveState = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo("register?ref={ref}") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            var currentTab by remember { mutableStateOf("home") }

            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        selectedRoute = currentTab,
                        onTabSelected = { tab ->
                            currentTab = tab
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        "home" -> HomeScreen(
                            viewModel = homeViewModel,
                            onNavigateToTab = { route ->
                                if (route == "support") {
                                    navController.navigate("support")
                                }
                            }
                        )
                        "team" -> TeamScreen(
                            viewModel = taskViewModel
                        )
                        "blog" -> BlogScreen(
                            viewModel = blogViewModel
                        )
                        "mine" -> MineScreen(
                            mineViewModel = mineViewModel,
                            authViewModel = authViewModel,
                            onNavigateToScreen = { route ->
                                navController.navigate(route)
                            },
                            onLogoutSuccess = {
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }

        composable("bank_card") {
            BankCardScreen(
                viewModel = mineViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("order_history") {
            OrderHistoryScreen(
                viewModel = mineViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("fund_history") {
            FundHistoryScreen(
                viewModel = mineViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("gift_code") {
            GiftCodeScreen(
                viewModel = mineViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("task") {
            TaskScreen(
                viewModel = taskViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("support") {
            SupportScreen(
                viewModel = mineViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
