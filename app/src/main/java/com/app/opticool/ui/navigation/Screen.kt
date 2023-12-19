package com.app.opticool.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Search: Screen("search")
    object Scan: Screen("scan")
    object Wishlist: Screen("wishlist")
    object Profile: Screen("profile")
    object DetailEyeglasses : Screen("home/{id}") {
        fun createRoute(id: Int) = "home/$id"
    }
    object SignUp: Screen("signup")
    object SignIn: Screen("signin")
}