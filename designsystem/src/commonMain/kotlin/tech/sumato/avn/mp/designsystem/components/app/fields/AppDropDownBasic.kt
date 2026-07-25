package tech.sumato.avn.mp.designsystem.components.app.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp


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
                    text = { Text(labelTransformer(it)) },
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