package com.app.opticool

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.app.opticool.presentation.detail.DetailScreen
import com.app.opticool.presentation.detail.DetailViewModel
import com.app.opticool.presentation.home.HomeScreen
import com.app.opticool.presentation.home.HomeViewModel
import com.app.opticool.ui.common.EyeglassState
import com.app.opticool.ui.common.EyeglassesState
import com.app.opticool.presentation.profile.ProfileScreen
import com.app.opticool.presentation.RecommendViewModel
import com.app.opticool.presentation.search.SearchScreen
import com.app.opticool.presentation.search.SearchViewModel
import com.app.opticool.presentation.signin.SignInScreen
import com.app.opticool.presentation.signup.SignUpScreen
import com.app.opticool.presentation.UserViewModel
import com.app.opticool.presentation.facePredictions.FacePredictionsEXP
import com.app.opticool.presentation.facePredictions.FacePredictionsScreen
import com.app.opticool.presentation.postToModel.DetectScreen
import com.app.opticool.presentation.postToModel.PostToModelViewModel
import com.app.opticool.presentation.postToModel.PreviewTakenImage
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
                val viewModel: HomeViewModel = viewModel(
                    factory = ViewModelFactory.getInstance(context)
                )
                LaunchedEffect(Unit) {
                    viewModel.getRecommendation()
                }
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    HomeScreen(
                        uiState = viewModel.uiState.collectAsState(initial = EyeglassesState.Loading).value,
                        retryAction = viewModel::getRecommendation,
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
                val viewModel: DetailViewModel = viewModel(
                    factory = ViewModelFactory.getInstance(context)
                )
                LaunchedEffect(Unit) {
                    viewModel.getDetail(id)
                }
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    DetailScreen(
                        uiState = viewModel.uiState.collectAsState(initial = EyeglassState.Loading).value,
                        retryAction = { id ->
                            viewModel.getDetail(id)
                        },
                        navigateBack = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            composable(Screen.Search.route) {
                val viewModel: SearchViewModel = viewModel(
                    factory = ViewModelFactory.getInstance(context)
                )
                LaunchedEffect(Unit) {
                    viewModel.getAllEyeglasses()
                }
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) }
                ) { innerPadding ->
                    SearchScreen(
                        uiState = viewModel.uiState.collectAsState(initial = EyeglassesState.Loading).value,
                        retryAction = viewModel::getAllEyeglasses,
                        navigateToDetail = { id ->
                            navController.navigate(Screen.DetailEyeglasses.createRoute(id))
                        },
                        query = viewModel.query.value,
                        onChangeQuery = viewModel::searchEyeglasses,
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

