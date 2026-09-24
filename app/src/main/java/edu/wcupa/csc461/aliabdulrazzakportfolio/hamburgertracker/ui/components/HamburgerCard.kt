package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.BurgerType
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.Hamburger
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerCheese
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerDarkBrown
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerLettuce
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerOrange
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerPatty
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerRed80
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerTomato
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.FavoriteRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HamburgerCard(
    hamburger: Hamburger,
    onEdit: (Hamburger) -> Unit,
    onDelete: (Hamburger) -> Unit,
    onFavoriteToggle: (Hamburger) -> Unit,
    modifier: Modifier = Modifier
) {
    val burgerType = runCatching { BurgerType.valueOf(hamburger.burgerType) }.getOrNull()

    val favoriteColor by animateColorAsState(
        targetValue = if (hamburger.isFavorite) FavoriteRed else Color.Gray,
        animationSpec = tween(300),
        label = "favorite_color"
    )

    val cardContainerColor by animateColorAsState(
        targetValue = if (hamburger.isFavorite)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(400),
        label = "card_color"
    )

    // Pulse animation for 5-star burgers
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (hamburger.rating == 5) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Card(
        onClick = { onEdit(hamburger) },
        modifier = modifier
            .fillMaxWidth()
            .scale(pulseScale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (hamburger.rating == 5) 6.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji avatar circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(typeToColor(hamburger.burgerType))
            ) {
                Text(
                    text = burgerType?.emoji ?: "\uD83C\uDF54",
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = hamburger.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (hamburger.price > 0) {
                        Text(
                            text = "$%.2f".format(hamburger.price),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (hamburger.restaurantName.isNotBlank()) {
                    Text(
                        text = "\uD83D\uDCCD ${hamburger.restaurantName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StarRatingBar(
                        rating = hamburger.rating,
                        starSize = 16.dp
                    )
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = burgerType?.label ?: hamburger.burgerType,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = typeToColor(hamburger.burgerType).copy(alpha = 0.25f)
                        )
                    )
                }

                if (hamburger.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = hamburger.description,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                        .format(Date(hamburger.dateAdded)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = { onFavoriteToggle(hamburger) }) {
                    Icon(
                        imageVector = if (hamburger.isFavorite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = favoriteColor
                    )
                }
                IconButton(onClick = { onDelete(hamburger) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun typeToColor(typeName: String): Color {
    return when (runCatching { BurgerType.valueOf(typeName) }.getOrNull()) {
        BurgerType.CLASSIC -> BurgerCheese
        BurgerType.BBQ -> BurgerPatty
        BurgerType.VEGGIE -> BurgerLettuce
        BurgerType.SPICY -> BurgerTomato
        BurgerType.GOURMET -> BurgerOrange
        BurgerType.DOUBLE -> BurgerDarkBrown
        BurgerType.BREAKFAST -> BurgerRed80
        BurgerType.SPECIALTY -> BurgerOrange
        null -> BurgerCheese
    }
}
