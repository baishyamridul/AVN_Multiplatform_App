package tech.sumato.avn.mp.designsystem.components.app.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun <T> AppDropDownBasic(
    modifier: Modifier,
    options: List<T>,
    labelTransformer: (T) -> String = { it.toString() },
    onSelected: (T) -> Unit,
    selected: T? = null,
    content: @Composable (currentOption: T?) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<T?>(null) }

    LaunchedEffect(selected) {
        if (selected != null) {
            selectedItem = selected
        }
    }

    Box(
        modifier = modifier.clickable(
            onClick = {
                expanded = !expanded
            }
        )) {

        content(selectedItem)

        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .border(Dp.Hairline, MaterialTheme.colorScheme.outline),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(labelTransformer(it), style = MaterialTheme.typography.bodyMedium) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    onClick = {
                        selectedItem = it
                        onSelected(it)
                        expanded = false
                    },
                )
            }
        }

    }

}