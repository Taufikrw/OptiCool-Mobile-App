package com.app.opticool

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.opticool.ui.ViewModelFactory
import com.app.opticool.ui.components.BottomBar
import com.app.opticool.ui.components.FeatureBanner
import com.app.opticool.ui.navigation.NavigationItem
import com.app.opticool.ui.navigation.Screen
import com.app.opticool.ui.screen.DetailScreen
import com.app.opticool.ui.screen.HomeScreen
import com.app.opticool.ui.screen.RecommendViewModel
import com.app.opticool.ui.theme.interFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptiCoolApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        val recommendViewModel: RecommendViewModel = viewModel(
            factory = ViewModelFactory.getInstance(LocalContext.current)
        )
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                recommendViewModel.getEyeglasses()
                HomeScreen(
                    uiState = recommendViewModel.eyeglassesState,
                    retryAction = recommendViewModel::getEyeglasses,
                    modifier = Modifier
                        .background(Color.White),
                    navigateToDetail = { id ->
                        navController.navigate(Screen.DetailEyeglasses.createRoute(id))
                    }
                )
            }
           composable(
               route = Screen.DetailEyeglasses.route,
               arguments = listOf(navArgument("id") {
                   type = NavType.IntType
               })
           ) {
               val id = it.arguments?.getInt("id") ?: -1
               recommendViewModel.getDetail(id)
               DetailScreen(
                   uiState = recommendViewModel.detailState,
                   retryAction = { id ->
                       recommendViewModel.getDetail(id)
                   },
                   navigateBack = {
                       navController.navigateUp()
                   },
                   modifier = Modifier
                       .background(Color.White),
               )
           }
//            composable(Screen.Wishlist.route) {
//                WishlistScreen(
//                    navigationToDetail = { id ->
//                        navController.navigate(Screen.DetailProduct.createRoute(id))
//                    }
//                )
//            }
//            composable(Screen.Profile.route) {
//                ProfileScreen()
//            }
        }
    }
}