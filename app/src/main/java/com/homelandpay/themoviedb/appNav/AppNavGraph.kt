package com.homelandpay.themoviedb.appNav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.homelandpay.themoviedb.ui.screen.DetailScreen
import com.homelandpay.themoviedb.ui.screen.FavoritesScreen
import com.homelandpay.themoviedb.ui.screen.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN ){
        composable(Routes.HOME_SCREEN){
            HomeScreen(navController = navController)
        }
        composable(Routes.Favourite_SCREEN){
            FavoritesScreen(navController = navController)
        }
        composable(
            route = Routes.DETAIL_SCREEN,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Retrieve the argument
            val itemId = backStackEntry.arguments?.getInt("itemId")
            // Pass the argument to your screen
            itemId?.let { DetailScreen(navController = navController, itemId = it) }
        }
    }

}