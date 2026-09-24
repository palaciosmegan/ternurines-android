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
    onTabSelected: (BottomTab) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    NavigationBar(containerColor = colors.surfaceContainerLowest) {
        BottomTab.entries.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    TabIcon(tab, selected, badgeCount = if (tab == BottomTab.Cart) cartCount else 0)
                },
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

@Composable
private fun TabIcon(tab: BottomTab, selected: Boolean, badgeCount: Int) {
    val icon = if (selected) tab.selectedIcon else tab.icon

    if (badgeCount > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.semantics { contentDescription = "$badgeCount productos" }
                ) {
                    Text("$badgeCount")
                }
            }
        ) {
            Icon(icon, contentDescription = null)
        }
    } else {
        Icon(icon, contentDescription = null)
    }
}
