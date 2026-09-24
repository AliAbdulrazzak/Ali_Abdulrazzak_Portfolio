package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.BurgerType
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.StatsViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.components.StarRatingBar
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerCheese
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerLettuce
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerOrange
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerPatty
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerTomato
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.StarGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel,
    onBack: () -> Unit
) {
    val stats by viewModel.statsState.collectAsState()
    val ratingCounts by viewModel.ratingCounts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDCCA", fontSize = 22.sp)
                        Text("  Burger Stats", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero total count
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "\uD83C\uDF54", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stats.totalCount.toString(),
                        fontSize = 64.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Burgers Tracked",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }

            // Stats grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Filled.Star,
                    iconTint = StarGold,
                    label = "Avg Rating",
                    value = if (stats.averageRating > 0) "%.1f".format(stats.averageRating) else "—",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Filled.LocalOffer,
                    iconTint = BurgerLettuce,
                    label = "Highest Price",
                    value = if (stats.maxPrice > 0) "$%.2f".format(stats.maxPrice) else "—",
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Filled.Restaurant,
                    iconTint = BurgerOrange,
                    label = "Top Restaurant",
                    value = stats.topRestaurant.ifBlank { "—" },
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Filled.Tag,
                    iconTint = BurgerTomato,
                    label = "Favorite Type",
                    value = runCatching {
                        BurgerType.valueOf(stats.mostCommonType).label
                    }.getOrDefault(stats.mostCommonType.ifBlank { "—" }),
                    modifier = Modifier.weight(1f)
                )
            }

            // Average rating display
            if (stats.averageRating > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Average Rating", style = MaterialTheme.typography.labelLarge)
                            Text(
                                "%.1f / 5.0".format(stats.averageRating),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        StarRatingBar(
                            rating = stats.averageRating.toInt(),
                            starSize = 28.dp
                        )
                    }
                }
            }

            // Rating distribution chart
            if (ratingCounts.sum() > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = StarGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Rating Distribution",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        val maxCount = ratingCounts.maxOrNull()?.coerceAtLeast(1) ?: 1
                        ratingCounts.forEachIndexed { index, count ->
                            AnimatedRatingBar(
                                stars = index + 1,
                                count = count,
                                maxCount = maxCount,
                                color = when (index + 1) {
                                    5 -> BurgerLettuce
                                    4 -> BurgerCheese
                                    3 -> BurgerOrange
                                    2 -> BurgerTomato
                                    else -> BurgerPatty
                                }
                            )
                            if (index < 4) Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // Perfect burgers callout
            val fiveStarCount = ratingCounts.getOrNull(4) ?: 0
            if (fiveStarCount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = StarGold.copy(alpha = 0.15f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "\uD83C\uDFC6", fontSize = 36.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "$fiveStarCount Perfect Burger${if (fiveStarCount > 1) "s" else ""}!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "You've found the best of the best \u2B50",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            if (stats.totalCount == 0) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "\uD83C\uDF54", fontSize = 64.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No data yet — start tracking burgers!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AnimatedRatingBar(
    stars: Int,
    count: Int,
    maxCount: Int,
    color: Color
) {
    val animatedFraction = remember { Animatable(0f) }
    LaunchedEffect(count, maxCount) {
        animatedFraction.animateTo(
            targetValue = if (maxCount > 0) count.toFloat() / maxCount else 0f,
            animationSpec = tween(durationMillis = 800, delayMillis = (5 - stars) * 80)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$stars\u2605",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(32.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedFraction.value)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(10.dp))
                    .background(color)
            )
        }
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier
                .width(28.dp)
                .padding(start = 6.dp)
        )
    }
}
