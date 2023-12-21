package com.app.opticool.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.opticool.R
import com.app.opticool.ui.navigation.NavigationItem
import com.app.opticool.ui.navigation.Screen
import com.app.opticool.ui.theme.interFontFamily

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
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
            title = "Wishlist",
            icon = Icons.Rounded.Favorite,
            screen = Screen.Wishlist
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Rounded.Person,
            screen = Screen.Profile
        )
    )
    Row(
        modifier = Modifier
            .shadow(elevation = 5.dp)
            .background(Color.White)
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.forEachIndexed { index, item ->
            if (index == 2) {
                Icon(
                    painterResource(id = R.drawable.ic_scan), contentDescription = null,
                    tint = Color(0xFF00DFA2),
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                )
            } else {
                AddItem(
                    screen = item.screen,
                    icon = item.icon,
                    title = item.title,
                    currentDestination = currentRoute,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: Screen,
    icon: ImageVector,
    title: String,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val background =
        if (selected) Color(0xFF0079FF) else Color.Transparent
    val contentColor =
        if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .height(52.dp)
            .width(50.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(background)
            .padding(
                start = 5.dp,
                end = 5.dp,
                top = 5.dp,
                bottom = 7.dp
            )
            .clickable {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "icon",
                tint = contentColor
            )
            Text(
                text = title,
                color = contentColor,
                fontFamily = interFontFamily,
                fontSize = 11.sp
            )
        }
    }
}