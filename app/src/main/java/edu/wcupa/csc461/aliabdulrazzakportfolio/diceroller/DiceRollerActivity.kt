package edu.wcupa.csc461.aliabdulrazzakportfolio.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.wcupa.csc461.aliabdulrazzakportfolio.diceroller.ui.theme.DiceRollerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiceRollerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceRollerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiceRollerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class RollRecord(val values: List<Int>, val sum: Int)

@Composable
fun DiceRollerApp(modifier: Modifier = Modifier) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    var diceCount by remember { mutableIntStateOf(1) }
    var diceResults by remember { mutableStateOf(listOf(1)) }
    var rollCount by remember { mutableIntStateOf(0) }
    var history by remember { mutableStateOf(listOf<RollRecord>()) }
    var faceCounts by remember { mutableStateOf(mapOf<Int, Int>()) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lastValue by remember { mutableIntStateOf(-1) }
    var showStats by remember { mutableStateOf(false) }
    var isRolling by remember { mutableStateOf(false) }

    val die1Rotation = remember { Animatable(0f) }
    val die2Rotation = remember { Animatable(0f) }
    val die1Scale = remember { Animatable(1f) }
    val die2Scale = remember { Animatable(1f) }
    val streakBadgeScale = remember { Animatable(1f) }

    val glowTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val isSpecialRoll = rollCount > 0 && diceResults.any { it == 1 || it == 6 }

    fun rollDice() {
        if (isRolling) return
        scope.launch {
            isRolling = true
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

            val newResults = (1..diceCount).map { (1..6).random() }

            launch {
                die1Rotation.animateTo(
                    die1Rotation.value + 360f + (15..75).random(),
                    animationSpec = tween(520, easing = FastOutSlowInEasing)
                )
            }
            launch {
                die1Scale.animateTo(1.3f, animationSpec = tween(90))
                die1Scale.animateTo(
                    1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }

            if (diceCount >= 2) {
                launch {
                    die2Rotation.animateTo(
                        die2Rotation.value + 360f + (15..75).random(),
                        animationSpec = tween(580, easing = FastOutSlowInEasing)
                    )
                }
                launch {
                    die2Scale.animateTo(1.3f, animationSpec = tween(110))
                    die2Scale.animateTo(
                        1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
            }

            delay(260)
            diceResults = newResults
            rollCount++

            val updated = faceCounts.toMutableMap()
            newResults.forEach { v -> updated[v] = (updated[v] ?: 0) + 1 }
            faceCounts = updated

            history = (listOf(RollRecord(newResults, newResults.sum())) + history).take(6)

            val first = newResults.first()
            streak = if (first == lastValue) streak + 1 else 1
            lastValue = first

            if (streak > 1) {
                launch {
                    streakBadgeScale.animateTo(1.6f, animationSpec = tween(130))
                    streakBadgeScale.animateTo(
                        1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                }
            }

            val streakBonus = if (streak > 1) (streak - 1) * 2 else 0
            score += newResults.sum() + streakBonus

            isRolling = false
        }
    }

    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ali's Dice Roller",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatChip(label = "Rolls", value = "$rollCount")
            StatChip(label = "Score", value = "$score", animated = true)
            AnimatedVisibility(visible = streak > 1) {
                Box(
                    modifier = Modifier
                        .scale(streakBadgeScale.value)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Streak x$streak",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiceFace(
                value = diceResults[0],
                rotation = die1Rotation.value % 360f,
                scale = die1Scale.value,
                glowAlpha = if (isSpecialRoll && (diceResults[0] == 1 || diceResults[0] == 6)) glowAlpha else 0f,
                isSpecial = isSpecialRoll && (diceResults[0] == 1 || diceResults[0] == 6)
            )

            AnimatedVisibility(
                visible = diceCount >= 2 && diceResults.size >= 2,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                if (diceResults.size >= 2) {
                    DiceFace(
                        value = diceResults[1],
                        rotation = die2Rotation.value % 360f,
                        scale = die2Scale.value,
                        glowAlpha = if (isSpecialRoll && (diceResults[1] == 1 || diceResults[1] == 6)) glowAlpha else 0f,
                        isSpecial = isSpecialRoll && (diceResults[1] == 1 || diceResults[1] == 6)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            targetState = diceLabel(diceResults, rollCount),
            transitionSpec = {
                (slideInVertically { -it } + fadeIn()) togetherWith
                    (slideOutVertically { it } + fadeOut())
            },
            label = "resultLabel"
        ) { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(visible = diceCount >= 2 && rollCount > 0) {
            AnimatedContent(
                targetState = "Sum: ${diceResults.sum()}",
                transitionSpec = {
                    (slideInVertically { -it } + fadeIn()) togetherWith
                        (slideOutVertically { it } + fadeOut())
                },
                label = "sumLabel"
            ) { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        DiceCountSelector(
            selected = diceCount,
            onSelect = { count ->
                diceCount = count
                diceResults = List(count) { idx ->
                    if (idx < diceResults.size) diceResults[idx] else 1
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { rollDice() },
            enabled = !isRolling,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(56.dp)
        ) {
            AnimatedContent(
                targetState = isRolling,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "rollBtnLabel"
            ) { rolling ->
                Text(
                    text = if (rolling) "Rolling..." else "Roll Dice",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimatedVisibility(visible = rollCount > 0) {
            TextButton(
                onClick = {
                    diceResults = List(diceCount) { 1 }
                    rollCount = 0
                    score = 0
                    streak = 0
                    lastValue = -1
                    history = emptyList()
                    faceCounts = emptyMap()
                    showStats = false
                }
            ) {
                Text("Reset", color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = history.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Recent Rolls",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(history) { record ->
                        HistoryChip(record = record)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(visible = rollCount > 0) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { showStats = !showStats }) {
                    Text(
                        text = if (showStats) "Hide Statistics" else "Show Statistics",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                AnimatedVisibility(
                    visible = showStats,
                    enter = slideInVertically { -it / 2 } + fadeIn(),
                    exit = slideOutVertically { -it / 2 } + fadeOut()
                ) {
                    StatsPanel(
                        faceCounts = faceCounts,
                        totalRolls = rollCount * diceCount
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun StatChip(label: String, value: String, animated: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        if (animated) {
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    (slideInVertically { -it } + fadeIn()) togetherWith
                        (slideOutVertically { it } + fadeOut())
                },
                label = "animatedStatChip"
            ) { v ->
                Text(
                    text = v,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DiceCountSelector(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
    ) {
        listOf(1 to "1 Die", 2 to "2 Dice").forEach { (count, label) ->
            val isSelected = selected == count
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                    .clickable { onSelect(count) }
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = label,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun DiceFace(
    value: Int,
    rotation: Float = 0f,
    scale: Float = 1f,
    glowAlpha: Float = 0f,
    isSpecial: Boolean = false,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSpecial) MaterialTheme.colorScheme.tertiary
    else MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .size(150.dp)
            .scale(scale)
            .rotate(rotation)
            .shadow(if (isSpecial) 16.dp else 8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 3.dp,
                color = borderColor.copy(
                    alpha = if (isSpecial) 0.35f + glowAlpha * 0.65f else 1f
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        PipLayout(value = value, pipColor = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun PipLayout(value: Int, pipColor: Color) {
    val layout = when (value) {
        1 -> booleanArrayOf(false, false, false, false, true,  false, false, false, false)
        2 -> booleanArrayOf(true,  false, false, false, false, false, false, false, true)
        3 -> booleanArrayOf(true,  false, false, false, true,  false, false, false, true)
        4 -> booleanArrayOf(true,  false, true,  false, false, false, true,  false, true)
        5 -> booleanArrayOf(true,  false, true,  false, true,  false, true,  false, true)
        6 -> booleanArrayOf(true,  false, true,  true,  false, true,  true,  false, true)
        else -> BooleanArray(9) { false }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        for (row in 0..2) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0..2) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (layout[row * 3 + col]) Pip(color = pipColor)
                    }
                }
            }
        }
    }
}

@Composable
fun Pip(color: Color) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(RoundedCornerShape(50))
            .background(color)
    )
}

@Composable
fun HistoryChip(record: RollRecord) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        record.values.forEach { v ->
            Text(
                text = "$v",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = when (v) {
                    6 -> MaterialTheme.colorScheme.tertiary
                    1 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
        if (record.values.size > 1) {
            Text(
                text = "=${record.sum}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun StatsPanel(faceCounts: Map<Int, Int>, totalRolls: Int) {
    val maxCount = (faceCounts.values.maxOrNull() ?: 0).toFloat().coerceAtLeast(1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            .padding(16.dp)
    ) {
        Text(
            text = "Roll Statistics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        for (face in 1..6) {
            val count = faceCounts[face] ?: 0
            val ratio = if (totalRolls > 0) count.toFloat() / totalRolls else 0f
            StatBar(
                face = face,
                count = count,
                ratio = ratio,
                barFraction = count / maxCount
            )
            if (face < 6) Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
fun StatBar(face: Int, count: Int, ratio: Float, barFraction: Float) {
    val animatedFraction by animateFloatAsState(
        targetValue = barFraction,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "statBar$face"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$face",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(18.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedFraction)
                    .clip(RoundedCornerShape(7.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$count (${(ratio * 100).toInt()}%)",
            fontSize = 11.sp,
            modifier = Modifier.width(60.dp),
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.End
        )
    }
}

fun diceLabel(values: List<Int>, rollCount: Int): String {
    if (rollCount == 0) return "Press Roll to start!"
    return when {
        values.size > 1 && values.all { it == 6 } -> "All Sixes! Legendary!"
        values.size > 1 && values.all { it == 1 } -> "Snake Eyes! Yikes!"
        values.first() == 6 -> "Six — Maximum Roll!"
        values.first() == 1 -> "One — Lucky Roll!"
        values.first() == 5 -> "Five — Almost Max!"
        values.first() == 4 -> "Four"
        values.first() == 3 -> "Three"
        values.first() == 2 -> "Two"
        else -> ""
    }
}
