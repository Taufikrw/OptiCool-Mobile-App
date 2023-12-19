package com.app.opticool

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.app.opticool.data.preferences.UserPreferences
import com.app.opticool.data.preferences.dataStore
import com.app.opticool.ui.ViewModelFactory
import com.app.opticool.ui.components.BottomBar
import com.app.opticool.ui.navigation.Screen
import com.app.opticool.ui.screen.DetailScreen
import com.app.opticool.ui.screen.HomeScreen
import com.app.opticool.ui.screen.ProfileScreen
import com.app.opticool.ui.screen.RecommendViewModel
import com.app.opticool.ui.screen.SearchScreen
import com.app.opticool.ui.screen.SignInScreen
import com.app.opticool.ui.screen.SignUpScreen
import com.app.opticool.ui.screen.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptiCoolApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val recommendViewModel: RecommendViewModel = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )
    val userViewModel: UserViewModel = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )
    val token = userViewModel.getToken().observeAsState()
    NavHost(
        navController = navController,
        startDestination = if (!token.value.isNullOrEmpty()) {
            "content"
        } else {
            "auth"
        },
        route = "root"
    ) {
        navigation(
            startDestination = Screen.SignIn.route,
            route = "auth"
        ) {
            composable(Screen.SignIn.route) {
                SignInScreen(
                    uiState = userViewModel.userState,
                    onLoginClicked = { userViewModel.login(it) },
                    onLoginSuccess = {
                        navController.navigate("content") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    },
                    saveSession = {
                        scope.launch {
                            userViewModel.saveToken(it)
                        }
                        ViewModelFactory.clearInstance()
                    },
                    navigateToSignUp = { navController.navigate("signup") },
                )
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    navController = navController
                )
            }
        }
        navigation(
            startDestination = Screen.Home.route,
            route = "content"
        ) {
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
            composable(Screen.Search.route) {
                Button(onClick = { navController.navigate("home") }) {
                    Text(text = "Click")
                }
            }
            composable(Screen.Profile.route) {
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    ProfileScreen(
                        onLogout = {
                            userViewModel.logout()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}