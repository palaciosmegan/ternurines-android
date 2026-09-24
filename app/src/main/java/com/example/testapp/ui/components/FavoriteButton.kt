package com.example.testapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    FilledIconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onToggle() },
        modifier = modifier,
        colors = IconButtonDefaults.filledIconToggleButtonColors(
            containerColor = colors.surfaceContainerLowest,
            contentColor = colors.primary,
            checkedContainerColor = colors.surfaceContainerLowest,
            checkedContentColor = colors.primary
        )
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos"
        )
    }
}
