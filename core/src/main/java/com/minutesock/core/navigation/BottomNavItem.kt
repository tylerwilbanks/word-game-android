package com.minutesock.core.navigation

import com.minutesock.core.R

sealed class BottomNavItem(
    val titleId: Int,
    val icon: Int,
    val route: String,
) {
    data object Daily : BottomNavItem(
        titleId = R.string.daily,
        icon = R.drawable.baseline_today_24,
        route = "daily"
    )

    data object Dictionary : BottomNavItem(
        titleId = R.string.dictionary,
        icon = R.drawable.baseline_menu_book_24,
        route = "dictionary"
    )

    data object Profile : BottomNavItem(
        titleId = R.string.profile,
        icon = R.drawable.baseline_person_24,
        route = "profile"
    )
}
