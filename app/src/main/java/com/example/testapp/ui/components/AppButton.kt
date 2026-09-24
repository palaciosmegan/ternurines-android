package com.example.testapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class AppButtonStyle { Primary, Secondary, Inverted, Outlined }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AppButtonStyle = AppButtonStyle.Primary,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val buttonModifier = modifier.heightIn(min = 48.dp)
    val padding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)

    if (style == AppButtonStyle.Outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = CircleShape,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            contentPadding = padding
        ) {
            ButtonContent(text, icon)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = CircleShape,
            colors = colorsFor(style),
            contentPadding = padding
        ) {
            ButtonContent(text, icon)
        }
    }
}

@Composable
private fun ButtonContent(text: String, icon: ImageVector?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun colorsFor(style: AppButtonStyle): ButtonColors {
    val colors = MaterialTheme.colorScheme
    return when (style) {
        AppButtonStyle.Secondary -> ButtonDefaults.buttonColors(
            containerColor = colors.surfaceVariant,
            contentColor = colors.onSurfaceVariant
        )
        AppButtonStyle.Inverted -> ButtonDefaults.buttonColors(
            containerColor = colors.inverseSurface,
            contentColor = colors.inverseOnSurface
        )
        else -> ButtonDefaults.buttonColors()
    }
}
