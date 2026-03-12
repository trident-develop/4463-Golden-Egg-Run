package com.pinger.textf.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pinger.textf.R
import com.pinger.textf.audio.VibrationHelper
import com.pinger.textf.components.GameBackground
import com.pinger.textf.components.MenuButton
import com.pinger.textf.components.ScreenTitle
import com.pinger.textf.components.SquareButton
import com.pinger.textf.components.formatTime
import com.pinger.textf.data.LevelProvider
import com.pinger.textf.model.GameState
import com.pinger.textf.model.Level
import com.pinger.textf.storage.GamePreferences
import com.pinger.textf.ui.theme.GameFont
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun GameScreen(
    levelId: Int,
    prefs: GamePreferences,
    vibrationHelper: VibrationHelper,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onNextLevel: (Int) -> Unit
) {
    val level = remember(levelId) { LevelProvider.getLevel(levelId) }

    var gameState by remember(levelId) {
        mutableStateOf(GameState(level = level))
    }

    var showPause by remember { mutableStateOf(false) }
    var showVictory by remember { mutableStateOf(false) }
    var elapsedMillis by remember(levelId) { mutableLongStateOf(0L) }
    var isExitingScreen by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        isExitingScreen = true
        onBack()
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE && !showVictory && !isExitingScreen)
                showPause = true
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    // Timer
    LaunchedEffect(levelId, showPause, showVictory) {
        while (!showPause && !showVictory) {
            delay(100L)
            elapsedMillis += 100L
        }
    }

    // Grid appearance animation
    val gridAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600),
        label = "gridAlpha"
    )

    fun resetLevel() {
        gameState = GameState(level = level)
        elapsedMillis = 0L
        showVictory = false
        showPause = false
    }

    fun onLevelComplete() {
        showVictory = true
        prefs.saveBestTime(levelId, elapsedMillis)
        if (levelId < 40) {
            prefs.unlockNextLevel(levelId)
        }
        vibrationHelper.vibrateSuccess()
    }

    GameBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SquareButton(
                    btnRes = R.drawable.back_button,
                    btnMaxWidth = 0.17f,
                    btnClickable = {
                        isExitingScreen = true
                        onBack()
                    }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Level $levelId",
                        fontFamily = GameFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    // Timer
                    Box {
                        Image(
                            painter = painterResource(R.drawable.time_bg),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .width(140.dp)
                                .height(32.dp)
                        )
                        Text(
                            text = formatTime(elapsedMillis),
                            fontFamily = GameFont,
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }

                SquareButton(
                    btnRes = R.drawable.pause_button,
                    btnMaxWidth = 0.38f,
                    btnClickable = { showPause = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Game board
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                ColorFlowBoard(
                    level = level,
                    gameState = gameState,
                    alpha = gridAlpha,
                    vibrationHelper = vibrationHelper,
                    onStateChanged = { newState ->
                        gameState = newState
                        if (newState.isAllConnected()) {
                            onLevelComplete()
                        }
                    }
                )
            }
        }

        // Pause overlay
        if (showPause) {
            PauseOverlay(
                onResume = { showPause = false },
                onReplay = { resetLevel() },
                onHome = {
                    isExitingScreen = true
                    onHome()
                }
            )
        }

        // Victory overlay
        AnimatedVisibility(
            visible = showVictory,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut()
        ) {
            VictoryOverlay(
                timeMillis = elapsedMillis,
                isLastLevel = levelId >= 40,
                onNextLevel = { onNextLevel(levelId + 1) },
                onReplay = { resetLevel() },
                onHome = {
                    isExitingScreen = true
                    onHome()
                }
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun ColorFlowBoard(
    level: Level,
    gameState: GameState,
    alpha: Float,
    vibrationHelper: VibrationHelper,
    onStateChanged: (GameState) -> Unit
) {
    val density = LocalDensity.current

    // Dot pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "dotPulse")
    val dotPulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotPulse"
    )

    // Recently completed color for flash effect
    var flashColor by remember { mutableStateOf<Color?>(null) }
    val flashAlpha by animateFloatAsState(
        targetValue = if (flashColor != null) 0f else 1f,
        animationSpec = tween(500),
        finishedListener = { flashColor = null },
        label = "flash"
    )
    val latestGameState by rememberUpdatedState(gameState)

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer { this.alpha = alpha }
    ) {
        val boardSizePx = with(density) { maxWidth.toPx() }
        val cellSizePx = boardSizePx / level.gridSize

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(level, gameState.isCompleted) {
                    if (gameState.isCompleted) return@pointerInput

                    var activeColor: Color? = null
                    var currentPath: MutableList<Pair<Int, Int>> = mutableListOf()

                    detectDragGestures(
                        onDragStart = { offset ->
                            val state = latestGameState

                            val col = (offset.x / cellSizePx).toInt()
                                .coerceIn(0, level.gridSize - 1)
                            val row = (offset.y / cellSizePx).toInt()
                                .coerceIn(0, level.gridSize - 1)
                            val cell = row to col

                            val touchedPair = level.colorPairs.find {
                                it.start == cell || it.end == cell
                            }

                            if (touchedPair != null) {
                                activeColor = touchedPair.color
                                currentPath = mutableListOf(cell)

                                val newPaths = state.paths.toMutableMap()
                                newPaths[touchedPair.color] = currentPath.toList()

                                onStateChanged(
                                    state.copy(
                                        paths = newPaths,
                                        activeColor = touchedPair.color
                                    )
                                )
                            } else {
                                val occupied = state.occupiedCells()
                                val colorAtCell = occupied[cell]

                                if (colorAtCell != null) {
                                    activeColor = colorAtCell

                                    val existingPath = state.paths[colorAtCell] ?: emptyList()
                                    val idx = existingPath.indexOf(cell)

                                    currentPath = if (idx >= 0) {
                                        existingPath.take(idx + 1).toMutableList()
                                    } else {
                                        mutableListOf(cell)
                                    }

                                    val newPaths = state.paths.toMutableMap()
                                    newPaths[colorAtCell] = currentPath.toList()

                                    onStateChanged(
                                        state.copy(
                                            paths = newPaths,
                                            activeColor = colorAtCell
                                        )
                                    )
                                } else {
                                    activeColor = null
                                    currentPath = mutableListOf()
                                }
                            }
                        },
                        onDrag = { change, _ ->
                            val ac = activeColor ?: return@detectDragGestures
                            val state = latestGameState
                            change.consume()

                            val col = (change.position.x / cellSizePx).toInt()
                                .coerceIn(0, level.gridSize - 1)
                            val row = (change.position.y / cellSizePx).toInt()
                                .coerceIn(0, level.gridSize - 1)
                            val cell = row to col

                            if (currentPath.isEmpty()) return@detectDragGestures

                            val lastCell = currentPath.last()
                            if (cell == lastCell) return@detectDragGestures

                            // Backtracking
                            if (currentPath.size >= 2 && cell == currentPath[currentPath.size - 2]) {
                                currentPath.removeAt(currentPath.size - 1)

                                val newPaths = state.paths.toMutableMap()
                                newPaths[ac] = currentPath.toList()

                                onStateChanged(state.copy(paths = newPaths, activeColor = ac))
                                return@detectDragGestures
                            }

                            // Only orthogonal movement
                            val dr = abs(cell.first - lastCell.first)
                            val dc = abs(cell.second - lastCell.second)
                            if (dr + dc != 1) return@detectDragGestures

                            // Cannot revisit own path
                            if (cell in currentPath) return@detectDragGestures

                            val occupied = state.occupiedCells()
                            val occupyingColor = occupied[cell]

                            if (occupyingColor != null && occupyingColor != ac) {
                                val newPaths = state.paths.toMutableMap()
                                newPaths.remove(occupyingColor)

                                currentPath.add(cell)
                                newPaths[ac] = currentPath.toList()

                                onStateChanged(state.copy(paths = newPaths, activeColor = ac))
                                return@detectDragGestures
                            }

                            val pair = level.colorPairs.find { it.color == ac }
                            val isEndpoint = pair != null &&
                                    (cell == pair.start || cell == pair.end) &&
                                    cell != currentPath.first()

                            currentPath.add(cell)

                            val newPaths = state.paths.toMutableMap()
                            newPaths[ac] = currentPath.toList()

                            val newState = state.copy(
                                paths = newPaths,
                                activeColor = ac
                            )
                            onStateChanged(newState)

                            if (isEndpoint) {
                                Log.d("MYTAG", "Endpoint reached: $cell")
                                vibrationHelper.vibrateShort()
                                flashColor = ac
                            }
                        },
                        onDragEnd = {
                            activeColor = null
                            onStateChanged(latestGameState.copy(activeColor = null))
                        },
                        onDragCancel = {
                            activeColor = null
                            onStateChanged(latestGameState.copy(activeColor = null))
                        }
                    )
                }
        ) {
            val cs = cellSizePx
            val gs = level.gridSize

            // Background grid
            for (row in 0 until gs) {
                for (col in 0 until gs) {
                    val x = col * cs
                    val y = row * cs
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.08f),
                        topLeft = Offset(x + 2f, y + 2f),
                        size = androidx.compose.ui.geometry.Size(cs - 4f, cs - 4f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                    )
                }
            }

            // Draw paths
            for ((color, path) in gameState.paths) {
                if (path.size < 2) continue
                val isFlashing = color == flashColor

                for (i in 0 until path.size - 1) {
                    val (r1, c1) = path[i]
                    val (r2, c2) = path[i + 1]
                    val start = Offset(c1 * cs + cs / 2, r1 * cs + cs / 2)
                    val end = Offset(c2 * cs + cs / 2, r2 * cs + cs / 2)

                    // Path glow
                    drawLine(
                        color = color.copy(alpha = 0.2f),
                        start = start,
                        end = end,
                        strokeWidth = cs * 0.45f,
                        cap = StrokeCap.Round
                    )
                    // Path main
                    drawLine(
                        color = color.copy(alpha = if (isFlashing) (0.6f + flashAlpha * 0.4f) else 0.7f),
                        start = start,
                        end = end,
                        strokeWidth = cs * 0.3f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // Draw color dots
            for (pair in level.colorPairs) {
                val isConnected = gameState.paths[pair.color]?.let { path ->
                    path.size >= 2 && path.first() == pair.start && path.last() == pair.end
                } ?: false

                val isActive = gameState.activeColor == pair.color
                val pulse = if (isActive) dotPulse else 1f

                drawColorDot(pair.start, pair.color, cs, pulse, isConnected)
                drawColorDot(pair.end, pair.color, cs, pulse, isConnected)
            }
        }
    }
}

private fun DrawScope.drawColorDot(
    cell: Pair<Int, Int>,
    color: Color,
    cellSize: Float,
    pulse: Float,
    isConnected: Boolean
) {
    val center = Offset(
        cell.second * cellSize + cellSize / 2,
        cell.first * cellSize + cellSize / 2
    )
    val baseRadius = cellSize * 0.28f
    val radius = baseRadius * pulse

    // Outer glow
    drawCircle(
        color = color.copy(alpha = if (isConnected) 0.3f else 0.15f),
        radius = radius * 2f,
        center = center
    )
    // Mid glow
    drawCircle(
        color = color.copy(alpha = if (isConnected) 0.5f else 0.3f),
        radius = radius * 1.4f,
        center = center
    )
    // Core
    drawCircle(
        color = color,
        radius = radius,
        center = center
    )
    // Highlight
    drawCircle(
        color = Color.White.copy(alpha = 0.4f),
        radius = radius * 0.35f,
        center = Offset(center.x - radius * 0.2f, center.y - radius * 0.2f)
    )
}

@Composable
private fun PauseOverlay(
    onResume: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenTitle(text = "Paused", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(24.dp))
            MenuButton(text = "Resume", onClick = onResume)
            MenuButton(text = "Replay", onClick = onReplay)
            MenuButton(text = "Home", onClick = onHome)
        }
    }
}

@Composable
private fun VictoryOverlay(
    timeMillis: Long,
    isLastLevel: Boolean,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit
) {
    // Particle burst animation
    val infiniteTransition = rememberInfiniteTransition(label = "victoryParticles")
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particles"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center
    ) {
        // Particle background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val colors = listOf(
                Color(0xFFE53935), Color(0xFF1E88E5), Color(0xFF43A047),
                Color(0xFFFDD835), Color(0xFFFF8F00), Color(0xFF8E24AA)
            )
            for (i in 0 until 20) {
                val angle = (i * 18f + particleProgress * 360f) * Math.PI / 180.0
                val dist = size.minDimension * 0.1f + particleProgress * size.minDimension * 0.35f
                val x = size.width / 2 + (dist * kotlin.math.cos(angle)).toFloat()
                val y = size.height / 2 + (dist * kotlin.math.sin(angle)).toFloat()
                val alpha = (1f - particleProgress).coerceIn(0f, 0.8f)
                drawCircle(
                    color = colors[i % colors.size].copy(alpha = alpha),
                    radius = 6f * (1f - particleProgress * 0.5f),
                    center = Offset(x, y)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Level Complete!",
                fontFamily = GameFont,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = Color(0xFFFDD835),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                Image(
                    painter = painterResource(R.drawable.time_bg),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(160.dp)
                        .height(40.dp)
                )
                Text(
                    text = formatTime(timeMillis),
                    fontFamily = GameFont,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (!isLastLevel) {
                MenuButton(text = "Next Level", onClick = onNextLevel)
            }
            MenuButton(text = "Replay", onClick = onReplay)
            MenuButton(text = "Home", onClick = onHome)
        }
    }
}