package com.gravitycode.solitaryfitness.util.ui.compose

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gravitycode.solitaryfitness.R

@Composable
fun OverflowMenu(menuItems: List<String>, onMenuItemClicked: (String) -> Unit) {
    val showMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            Toast.makeText(context, "onDismissRequest", Toast.LENGTH_SHORT).show()
            showMenu.value = false
        }
    ) {
        for (menuItem in menuItems) {
            DropdownMenuItem({ Text(menuItem) }, {
                showMenu.value = false
                onMenuItemClicked(menuItem)
            })
        }
    }
}