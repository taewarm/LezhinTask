package com.example.lezhintask.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lezhintask.ui.bookmark.BookMarkPage
import com.example.lezhintask.ui.search.SearchPage

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.SEARCH,
        enterTransition = {
            EnterTransition.None
        }, exitTransition = { ExitTransition.None }
    ) {
        composable(BottomNavItem.Search.screenRoute) { SearchPage() }
        composable(BottomNavItem.BookMark.screenRoute) { BookMarkPage() }
    }
}