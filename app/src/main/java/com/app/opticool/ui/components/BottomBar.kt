package com.app.opticool.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.opticool.ui.navigation.NavigationItem
import com.app.opticool.ui.navigation.Screen

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = "Home",
                icon = Icons.Rounded.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = "Search",
                icon = Icons.Rounded.Search,
                screen = Screen.Search
            ),
            NavigationItem(
                title = "Scan",
                icon = Icons.Rounded.AddCircle,
                screen = Screen.Scan
            ),
            NavigationItem(
                title = "Order",
                icon = Icons.Rounded.Home,
                screen = Screen.Order
            ),
            NavigationItem(
                title = "Profile",
                icon = Icons.Rounded.Person,
                screen = Screen.Profile
            )
        )
        navigationItems.map {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title
                    )
                },
                label = {
                    Text(it.title)
                },
                selected = currentRoute == it.screen.route,
                onClick = {
                    navController.navigate(it.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}