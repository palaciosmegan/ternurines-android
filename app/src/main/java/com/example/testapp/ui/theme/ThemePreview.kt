package com.example.testapp.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun ThemeShowcase() {
    val colors = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Column(
        modifier = Modifier
            .background(colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Quicksand", style = type.displaySmall, color = colors.onBackground)
        Text("Headline", style = type.headlineSmall, color = colors.onBackground)
        Text("Title large", style = type.titleLarge, color = colors.onBackground)
        Text(
            "Body large — The quick brown fox jumps over the lazy dog.",
            style = type.bodyLarge,
            color = colors.onBackground
        )
        Text("Label medium", style = type.labelMedium, color = colors.onSurfaceVariant)

        HorizontalDivider(color = colors.outlineVariant)

        SwatchRow(
            "Primary" to (colors.primary to colors.onPrimary),
            "Container" to (colors.primaryContainer to colors.onPrimaryContainer)
        )
        SwatchRow(
            "Secondary" to (colors.secondary to colors.onSecondary),
            "Container" to (colors.secondaryContainer to colors.onSecondaryContainer)
        )
        SwatchRow(
            "Tertiary" to (colors.tertiary to colors.onTertiary),
            "Container" to (colors.tertiaryContainer to colors.onTertiaryContainer)
        )
        SwatchRow(
            "Background" to (colors.background to colors.onBackground),
            "Surface" to (colors.surface to colors.onSurface),
            "Variant" to (colors.surfaceVariant to colors.onSurfaceVariant)
        )
        SwatchRow(
            "Low" to (colors.surfaceContainerLow to colors.onSurface),
            "Container" to (colors.surfaceContainer to colors.onSurface),
            "High" to (colors.surfaceContainerHigh to colors.onSurface),
            "Highest" to (colors.surfaceContainerHighest to colors.onSurface)
        )

        HorizontalDivider(color = colors.outlineVariant)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}) { Text("Button") }
            FilledTonalButton(onClick = {}) { Text("Tonal") }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = {}) { Text("Outlined") }
            TextButton(onClick = {}) { Text("Text") }
            Switch(checked = true, onCheckedChange = {})
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssistChip(onClick = {}, label = { Text("Chip") })
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
        OutlinedTextField(
            value = "Texto de ejemplo",
            onValueChange = {},
            label = { Text("Campo") },
            modifier = Modifier.fillMaxWidth()
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Card", style = type.titleMedium)
                Text("surfaceContainerHighest", style = type.bodyMedium)
            }
        }
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Elevated card", style = type.titleMedium)
                Text("surfaceContainerLow", style = type.bodyMedium)
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = colors.inverseSurface,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Inverse surface (snackbar)",
                color = colors.inverseOnSurface,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun SwatchRow(vararg swatches: Pair<String, Pair<Color, Color>>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        swatches.forEach { (name, pair) ->
            val (color, onColor) = pair
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .background(color, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(name, color = onColor, fontSize = 11.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(name = "Light", showBackground = true, heightDp = 1400)
@Composable
private fun ThemeLightPreview() {
    TestAppTheme(darkTheme = false) { ThemeShowcase() }
}

@Preview(
    name = "Dark",
    showBackground = true,
    heightDp = 1400,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ThemeDarkPreview() {
    TestAppTheme(darkTheme = true) { ThemeShowcase() }
}
