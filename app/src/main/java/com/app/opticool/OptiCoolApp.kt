package com.app.opticool

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.opticool.ui.ViewModelFactory
import com.app.opticool.ui.components.BottomBar
import com.app.opticool.ui.navigation.Screen
import com.app.opticool.ui.screen.DetailScreen
import com.app.opticool.ui.screen.HomeScreen
import com.app.opticool.ui.screen.RecommendViewModel
import com.app.opticool.ui.screen.SignInScreen
import com.app.opticool.ui.screen.SignUpScreen
import com.app.opticool.ui.screen.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptiCoolApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val recommendViewModel: RecommendViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
    val userViewModel: UserViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                uiState = userViewModel.userState,
                navController = navController,
                viewModel = userViewModel
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                navController = navController
            )
        }
        composable(Screen.Home.route) {
            recommendViewModel.getEyeglasses()
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) { innerPadding ->
                HomeScreen(
                    uiState = recommendViewModel.eyeglassesState,
                    retryAction = recommendViewModel::getEyeglasses,
                    navigateToDetail = { id ->
                        navController.navigate(Screen.DetailEyeglasses.createRoute(id))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        composable(
            route = Screen.DetailEyeglasses.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            val id = it.arguments?.getInt("id") ?: -1
            recommendViewModel.getDetail(id)
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) { innerPadding ->
                DetailScreen(
                    uiState = recommendViewModel.detailState,
                    retryAction = { id ->
                        recommendViewModel.getDetail(id)
                    },
                    navigateBack = {
                        navController.navigateUp()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}