package com.example.lezhintask.navigation

import com.example.lezhintask.R

sealed class BottomNavItem(val title: Int, val screenRoute: String) {
    object Search : BottomNavItem(R.string.search, SEARCH)
    object BookMark : BottomNavItem(R.string.bookmark, BOOKMARK)
    companion object {
        const val SEARCH = "SEARCH"
        const val BOOKMARK = "BOOKMARK"
    }
}
