package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.StarEmpty
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.StarGold

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingChange: ((Int) -> Unit)? = null,
    maxStars: Int = 5,
    starSize: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(maxStars) { index ->
            val starIndex = index + 1
            val isFilled = starIndex <= rating

            val scale by animateFloatAsState(
                targetValue = if (isFilled) 1f else 0.85f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "star_scale_$index"
            )

            Icon(
                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "$starIndex stars",
                tint = if (isFilled) StarGold else StarEmpty,
                modifier = Modifier
                    .size(starSize)
                    .scale(scale)
                    .then(
                        if (onRatingChange != null) {
                            Modifier.clickable { onRatingChange(starIndex) }
                        } else Modifier
                    )
            )
        }
    }
}
