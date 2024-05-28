package com.thatwaz.unloadpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thatwaz.unloadpro.ui.presentation.DisplayCounterScreen
import com.thatwaz.unloadpro.ui.presentation.MainScreen
import com.thatwaz.unloadpro.ui.presentation.SplashScreen
import com.thatwaz.unloadpro.viewmodel.ClickCounterViewModel
import com.thatwaz.unloadpro.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("main") {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(mainViewModel, navController)
        }
        composable(
            route = "displayCounter/{initialCartonCount}",
            arguments = listOf(navArgument("initialCartonCount") { type = NavType.IntType })
        ) { backStackEntry ->
            val clickCounterViewModel: ClickCounterViewModel = hiltViewModel()
            val initialCartonCount = backStackEntry.arguments?.getInt("initialCartonCount") ?: 0
            DisplayCounterScreen(clickCounterViewModel, initialCartonCount)
        }
    }
}






//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "splash") {
//        composable("splash") {
//            SplashScreen(navController)
//        }
//        composable("main") {
//            val viewModel: MainViewModel = hiltViewModel()
//            MainScreen(viewModel, navController)
//        }
//        composable("displayCounter") {
//            val viewModel: ClickCounterViewModel = hiltViewModel()
//            DisplayCounterScreen(viewModel)
//        }
//    }

//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "splash") {
//        composable("splash") { SplashScreen(navController) }
//        composable("main") {
//            val viewModel: MainViewModel = hiltViewModel()
//            MainScreen(viewModel, navController)
//        }
//        composable("displayCounter") {
//            val viewModel: ClickCounterViewModel = hiltViewModel()
//            DisplayCounterScreen(viewModel)
//        }
//    }






