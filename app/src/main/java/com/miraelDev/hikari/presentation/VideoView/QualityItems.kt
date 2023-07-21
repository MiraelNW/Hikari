package com.miraelDev.hikari.presentation.VideoView

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

data class DropItem(
    val text: String
)

@Composable
fun QualityItems(
    quality: String,
    dropdownItems: List<DropItem>,
    modifier: Modifier = Modifier,
    onMenuItemClick: (DropItem) -> Unit
) {

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    var itemWidth by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current

    Card(
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
                itemWidth = with(density) { it.width.toDp() }
            }
    ) {
        Text(
            modifier = Modifier
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                    )
                }
                .padding(16.dp),
            text = quality,
            color = MaterialTheme.colors.primary)
        DropdownMenu(
            modifier = Modifier.background(Color.Black.copy(alpha = 0.85f)),
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = pressOffset.copy(
                y = (pressOffset.y - itemHeight),
                x = pressOffset.x - itemWidth
            )
        ) {
            dropdownItems.forEach {
                DropdownMenuItem(onClick = {
                    onMenuItemClick(it)
                    isContextMenuVisible = false
                }) {
                    Text( text = it.text, color = Color.White)
                }
            }
        }
    }
}