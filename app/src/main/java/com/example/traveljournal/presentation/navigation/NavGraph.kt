package com.example.traveljournal.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.traveljournal.R
import com.example.traveljournal.presentation.screens.add.AddTripScreen
import com.example.traveljournal.presentation.screens.trips.TripsScreen
import com.example.traveljournal.presentation.screens.wishlist.WishlistScreen

sealed class Screen(
    val route: String,
    val title: Int,
    val icon: ImageVector
) {
    object Trips : Screen("trips", R.string.trips, Icons.Default.Home)
    object Add : Screen("add", R.string.add, Icons.Default.Add)
    object Wishlist : Screen("wishlist", R.string.wishlist, Icons.Default.Favorite)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Trips.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Trips.route) {
                TripsScreen(
                    onAddClick = { navController.navigate(Screen.Add.route) },
                    onTripClick = {}
                )
            }
            composable(Screen.Add.route) {
                AddTripScreen(navController = navController)
            }
            composable(Screen.Wishlist.route) {
                WishlistScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.Trips,
        Screen.Add,
        Screen.Wishlist
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.title)) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
