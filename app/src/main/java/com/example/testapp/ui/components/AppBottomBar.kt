package com.example.testapp.ui.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.testapp.navigation.BottomTab

@Composable
fun AppBottomBar(
    currentRoute: String?,
    cartCount: Int,
    onTabSelected: (BottomTab) -> Unit,
    favoritesCount: Int = 0,
    lowStockCount: Int = 0
) {
    val colors = MaterialTheme.colorScheme

    NavigationBar(containerColor = colors.surfaceContainerLowest) {
        BottomTab.entries.forEach { tab ->
            val selected = currentRoute == tab.route
            val badge = when (tab) {
                BottomTab.Cart -> TabBadge(cartCount, "$cartCount productos en el carrito")
                BottomTab.Favorites -> TabBadge(favoritesCount, "$favoritesCount favoritos")
                BottomTab.Profile -> TabBadge(
                    count = lowStockCount,
                    description = "$lowStockCount productos con stock bajo",
                    isAlert = true
                )
                else -> TabBadge(0, "")
            }
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = { TabIcon(tab, selected, badge) },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    selectedTextColor = colors.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

private data class TabBadge(val count: Int, val description: String, val isAlert: Boolean = false)

@Composable
private fun TabIcon(tab: BottomTab, selected: Boolean, badge: TabBadge) {
    val icon = if (selected) tab.selectedIcon else tab.icon

    if (badge.count > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = if (badge.isAlert) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.semantics { contentDescription = badge.description }
                ) {
                    Text("${badge.count}")
                }
            }
        ) {
            Icon(icon, contentDescription = null)
        }
    } else {
        Icon(icon, contentDescription = null)
    }
}
