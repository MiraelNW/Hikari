package com.miraelDev.vauma.presentation.animeInfoDetailAndPlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.miraelDev.vauma.R
import com.miraelDev.vauma.exntensions.noRippleEffectClick
import com.miraelDev.vauma.exntensions.pressClickEffect
import com.miraelDev.vauma.presentation.mainScreen.LocalTheme

@Composable
fun AnimeSeriesDialog(
    onDismiss: () -> Unit,
    onSeriesClick: (Int) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleEffectClick(onClick = onDismiss)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .systemBarsPadding()
                .padding(vertical = 32.dp)
                .noRippleEffectClick { }
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.background
        ) {
            Column(
                Modifier
                    .padding(12.dp)
                    .wrapContentSize(),
            ) {
                Text(
                    text = stringResource(R.string.choose_series),
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 32.sp,
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(120) {
                        SeriesElement(number = it, onSeriesClick = onSeriesClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesElement(
    number: Int,
    onSeriesClick: (Int) -> Unit
) {

    Box(
        modifier = Modifier
            .padding(6.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(24.dp)
            )
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .noRippleEffectClick {
                    onSeriesClick(number)
                }
                .padding(8.dp)
                .background(MaterialTheme.colors.background),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.widthIn(max = 100.dp),
                text = "Серия ${number + 1}",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}