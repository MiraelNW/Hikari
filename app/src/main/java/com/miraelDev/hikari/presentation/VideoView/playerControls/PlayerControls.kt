package com.miraelDev.hikari.presentation.VideoView.playerControls


import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.miraelDev.hikari.R
import com.miraelDev.hikari.entensions.noRippleEffectClick
import com.miraelDev.hikari.presentation.VideoView.DropItem
import com.miraelDev.hikari.ui.theme.DirtyWhite
import com.miraelDev.hikari.ui.theme.LightGreen

private const val PORTRAIT = 0
private const val LANDSCAPE = 1

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isPlaying: () -> Boolean,
    isFirstEpisode: Boolean,
    isLastEpisode: Boolean,
    isFullScreen: Int,
    orientation: Int,
    alpha: Float,
    title: String,
    onReplayClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    currTime: String,
    bufferedPercentage: () -> Int,
    onBackIconClick: () -> Unit,
    playbackState: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    onFullScreenToggle: (Int) -> Unit,
    onNextVideoClick: () -> Unit,
    onPreviousVideoClick: () -> Unit,
    onEpisodeItemClick: (Int) -> Unit,
    onEpisodeIconClick: () -> Unit,
    onCloseEpisodeList: () -> Unit,
    changeVisibleState: () -> Unit,
    onMenuItemClick: (DropItem) -> Unit,
    onOpenQualityMenu: () -> Unit,
    onAutoNextVideoClick: (Boolean) -> Unit,
) {


    val visible = remember(isVisible) {
        true
    }

    var quality by rememberSaveable {
        mutableStateOf("480")
    }

    var shouldShowEpisodeList by rememberSaveable() {
        mutableStateOf(false)
    }

    val isVideoPlaying = remember(isPlaying()) { isPlaying() }

    val playerState = remember(playbackState()) { playbackState() }

    val isFullScreenSaved = remember() { isFullScreen }

    val onBackIconClickSaved: () -> Unit = remember { { onBackIconClick() } }

    val onEpisodeIconClickSaved: () -> Unit = remember { { onEpisodeIconClick() } }

    val onAutoNextVideoClickSaved: (Boolean) -> Unit = remember { { onAutoNextVideoClick(it) } }

    val titleSaved = remember { title }

    Box(modifier = modifier) {

        Box(modifier = modifier.noRippleEffectClick(MutableInteractionSource()) {
            changeVisibleState()
            Log.d("tag", " main box click")
        }) {

            AnimatedVisibility(
                modifier = modifier,
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {

                    TopControl(
                        title = titleSaved,
                        onBackIconClick = onBackIconClickSaved,
                        onEpisodeIconClick = {
                            shouldShowEpisodeList = true
                            onEpisodeIconClickSaved()
                        },
                        onAutoLoadNextVideoClick = onAutoNextVideoClickSaved
                    )

                    BottomControls(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(
                                start = if (isFullScreen == LANDSCAPE) 24.dp else 8.dp
                            )
                            .animateEnterExit(
                                enter =
                                slideInVertically(
                                    initialOffsetY = { fullHeight: Int ->
                                        fullHeight
                                    }
                                ),
                                exit =
                                slideOutVertically(
                                    targetOffsetY = { fullHeight: Int ->
                                        fullHeight
                                    }
                                )
                            ),
                        quality = quality,
                        currTime = currTime,
                        totalDuration = totalDuration,
                        currentTime = currentTime,
                        isFullScreen = isFullScreen,
                        bufferedPercentage = bufferedPercentage,
                        onSeekChanged = onSeekChanged,
                        onFullScreenToggle = onFullScreenToggle,
                        onValueChangeFinished = onValueChangeFinished,
                        onMenuItemClick = { dropItem ->
                            quality = dropItem.text
                            onMenuItemClick(dropItem)
                        },
                        onOpenQualityMenu = onOpenQualityMenu
                    )
                }
            }
        }

//        CenterControls(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.5f)
//                .align(Alignment.Center),
//            onReplayClick = onReplayClick,
//            onForwardClick = onForwardClick,
//            changeVisibleState = changeVisibleState
//        )

        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                        Alignment.BottomCenter
                    else
                        Alignment.Center
                )
                .padding(bottom = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 16.dp else 0.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .noRippleEffectClick(
                        interactionSource = MutableInteractionSource(),
                        enabled = !isFirstEpisode
                    ) {
                        if (visible) {
                            onPreviousVideoClick()
                        }
                        changeVisibleState()
                    }
                    .size(32.dp)
                    .weight(1f),
                tint = if (isFirstEpisode) DirtyWhite.copy(alpha = 1f)
                else DirtyWhite.copy(alpha = 1f),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_previous_video),
                contentDescription = "next video"
            )

            Icon(
                modifier = Modifier
                    .noRippleEffectClick(interactionSource = MutableInteractionSource()) {
//                        onPauseToggle()
//                        changeVisibleState()
                    }
                    .size(42.dp)
                    .weight(1f),
                tint = DirtyWhite.copy(alpha = 1f),
                painter = painterResource(id = R.drawable.ic_pause),
//                when {
//                    isVideoPlaying -> {
//                        painterResource(id = R.drawable.ic_pause)
//                    }
//
//                    isVideoPlaying.not() && playerState == Player.STATE_ENDED -> {
//                        painterResource(id = R.drawable.ic_replay)
//                    }
//
//                    else -> {
//                        painterResource(id = R.drawable.ic_play)
//                    }
//                },
                contentDescription = "Play/pause"
            )

            Icon(
                modifier = Modifier
                    .noRippleEffectClick(
                        interactionSource = MutableInteractionSource(),
                        enabled = !isLastEpisode
                    ) {
                        if (visible) {
                            onNextVideoClick()
                        }
                        changeVisibleState()
                    }
                    .size(32.dp)
                    .weight(1f),
                tint = if (isLastEpisode) LightGreen.copy(alpha = 1f)
                else DirtyWhite.copy(alpha = 1f),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_next_video),
                contentDescription = "next video"
            )


        }

        EpisodeList(
            modifier = modifier,
            shouldShowEpisodeList = shouldShowEpisodeList,
            orientation = orientation,
            onCloseEpisodeList = {
                shouldShowEpisodeList = false
                onCloseEpisodeList()
            },
            onEpisodeItemClick = { episodeId ->
                onEpisodeItemClick(episodeId)
                onCloseEpisodeList()
            }
        )
    }
}





