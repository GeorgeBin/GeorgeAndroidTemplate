package com.georgebindragon.android.core.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
