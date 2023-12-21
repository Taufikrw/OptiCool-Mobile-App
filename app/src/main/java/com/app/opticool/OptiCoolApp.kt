package com.app.opticool

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.app.opticool.ui.ViewModelFactory
import com.app.opticool.ui.components.BottomBar
import com.app.opticool.ui.navigation.Screen
import com.app.opticool.ui.screen.DetailScreen
import com.app.opticool.ui.screen.EyeglassViewModel
import com.app.opticool.ui.screen.HomeScreen
import com.app.opticool.ui.screen.ProfileScreen
import com.app.opticool.ui.screen.RecommendViewModel
import com.app.opticool.ui.screen.SearchScreen
import com.app.opticool.ui.screen.SignInScreen
import com.app.opticool.ui.screen.SignUpScreen
import com.app.opticool.ui.screen.UserViewModel
import com.app.opticool.ui.screen.facePredictions.FacePredictionsEXP
import com.app.opticool.ui.screen.facePredictions.FacePredictionsScreen
import com.app.opticool.ui.screen.postToModel.DetectScreen
import com.app.opticool.ui.screen.postToModel.PostToModelViewModel
import com.app.opticool.ui.screen.postToModel.PreviewTakenImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import java.util.concurrent.ExecutorService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptiCoolApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    outputDirectory: File,
    cameraExecutor: ExecutorService,
) {
    var capturedImage by remember { mutableStateOf<File?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val eyeglassViewModel: EyeglassViewModel = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )
    val userViewModel: UserViewModel = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )
    val recommendViewModel: RecommendViewModel = viewModel(
        factory = ViewModelFactory.getInstance(context)
    )

    val postToModelViewModel: PostToModelViewModel = viewModel()


    val noBottomBarFab = listOf(
        Screen.TakeImage.route,
        Screen.PreviewTakenImage.route,
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
                    uiState = userViewModel.newUserState,
                    onRegisterClicked = { userViewModel.register(it) },
                    navigateToSigIn = { navController.navigate("signin") }
                )
            }
        }
        navigation(
            startDestination = Screen.Home.route,
            route = "content"
        ) {
            composable(Screen.Home.route) {
                eyeglassViewModel.getRecommendation()
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    HomeScreen(
                        uiState = eyeglassViewModel.eyeglassesState,
                        retryAction = eyeglassViewModel::getRecommendation,
                        navigateToDetail = { id ->
                            navController.navigate(Screen.DetailEyeglasses.createRoute(id))
                        },
                        navigateToSearch = { navController.navigate(Screen.Search.route) },
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
                eyeglassViewModel.getDetail(id)
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    DetailScreen(
                        uiState = eyeglassViewModel.detailState,
                        retryAction = { id ->
                            eyeglassViewModel.getDetail(id)
                        },
                        navigateBack = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            composable(Screen.Search.route) {
                eyeglassViewModel.getEyeglasses()
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    SearchScreen(
                        uiState = eyeglassViewModel.eyeglassesState,
                        retryAction = eyeglassViewModel::getEyeglasses,
                        navigateToDetail = { id ->
                            navController.navigate(Screen.DetailEyeglasses.createRoute(id))
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
            composable(Screen.Wishlist.route) {
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Under Construction")
                    }
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
            composable(Screen.Scan.route) {
                DetectScreen(
                    outputDirectory = outputDirectory,
                    cameraExecutor = cameraExecutor,
                    onImageCaptured = { file ->
                        capturedImage = file
                        Handler(Looper.getMainLooper()).post {
                            navController.navigate(Screen.PreviewTakenImage.route)
                        }
                    },
                    onError = {

                    },
                )
            }
            composable(Screen.PreviewTakenImage.route) {
                PreviewTakenImage(
                    postToModelViewModel = postToModelViewModel,
                    navController = navController,
                    photo = capturedImage,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.navigate(Screen.Home.route)
                    },
                    navigateToFacePredictions = { face ->
                        navController.navigate(Screen.FacePredictions.createRoute(face))
                    }
                )
            }

            composable(
                route = Screen.FacePredictions.route,
                arguments = listOf(navArgument("faceShape") { type = NavType.StringType }),
            ) {
                val face = it.arguments?.getString("faceShape")
                recommendViewModel.getEyeglasses(face!!.lowercase(Locale.getDefault()))
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { it ->
                    Column {
                        if (face != null){
                            FacePredictionsEXP(faceShape = face)
                        }
                        FacePredictionsScreen(
                            uiState = recommendViewModel.eyeglassesState,
                            retryAction = { },
                            navigateToDetail = { id ->
                                navController.navigate(Screen.DetailEyeglasses.createRoute(id))
                            },
                        )
                        Spacer(modifier = Modifier
                            .padding(it)
                            .height(20.dp))
                    }
                }
            }
        }
    }
}

