package com.gravitycode.solitaryfitness.util.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gravitycode.solitaryfitness.R

/**
 * @see [https://stackoverflow.com/a/68354402/4596649]
 * */
@Composable
fun OverflowMenu(content: @Composable () -> Unit) {
    val showMenu = remember { mutableStateOf(false) }

    IconButton(onClick = {
        showMenu.value = !showMenu.value
    }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.overflow_icon_content_description),
            tint = MaterialTheme.colorScheme.background
        )
    }
    DropdownMenu(
        expanded = showMenu.value,
        onDismissRequest = {
            showMenu.value = false
        }
    ) {
        content()
    }
}