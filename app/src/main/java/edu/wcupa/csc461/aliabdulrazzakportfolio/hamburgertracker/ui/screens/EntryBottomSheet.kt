package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.BurgerType
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.Hamburger
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.EntryViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.components.StarRatingBar
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.FavoriteRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBottomSheet(
    entryId: Long,
    viewModel: EntryViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var restaurant by rememberSaveable { mutableStateOf("") }
    var selectedType by rememberSaveable { mutableStateOf(BurgerType.CLASSIC) }
    var rating by rememberSaveable { mutableIntStateOf(0) }
    var price by rememberSaveable { mutableStateOf("") }
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    // Load existing hamburger when editing
    if (entryId != 0L) {
        val existingHamburger by viewModel.getHamburgerStream(entryId).collectAsState(initial = null)
        LaunchedEffect(existingHamburger) {
            existingHamburger?.let { h ->
                name = h.name
                description = h.description
                restaurant = h.restaurantName
                selectedType = runCatching { BurgerType.valueOf(h.burgerType) }.getOrDefault(BurgerType.CLASSIC)
                rating = h.rating
                price = if (h.price > 0) "%.2f".format(h.price) else ""
                isFavorite = h.isFavorite
            }
        }
    }

    fun save() {
        if (name.isBlank()) {
            nameError = true
            return
        }
        viewModel.saveHamburger(
            id = entryId,
            name = name.trim(),
            description = description.trim(),
            restaurantName = restaurant.trim(),
            burgerType = selectedType,
            rating = rating,
            price = price.toDoubleOrNull() ?: 0.0,
            isFavorite = isFavorite
        )
        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (entryId == 0L) "\uD83C\uDF54 Add Hamburger" else "\u270F\uFE0F Edit Hamburger",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) FavoriteRed else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Burger Name *") },
                isError = nameError,
                supportingText = if (nameError) ({ Text("Name is required") }) else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = restaurant,
                onValueChange = { restaurant = it },
                label = { Text("Restaurant / Location") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Burger type selector
            Box {
                OutlinedTextField(
                    value = "${selectedType.emoji} ${selectedType.label}",
                    onValueChange = {},
                    label = { Text("Burger Type") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { typeMenuExpanded = true }
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .clickable { typeMenuExpanded = true })
                DropdownMenu(
                    expanded = typeMenuExpanded,
                    onDismissRequest = { typeMenuExpanded = false }
                ) {
                    BurgerType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text("${type.emoji} ${type.label}")
                            },
                            onClick = {
                                selectedType = type
                                typeMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Animated star rating selector
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn()
            ) {
                Column {
                    Text(
                        text = "Your Rating",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StarRatingBar(
                            rating = rating,
                            onRatingChange = { rating = it },
                            starSize = 36.dp
                        )
                        Text(
                            text = when (rating) {
                                0 -> "Tap to rate"
                                1 -> "Terrible \uD83D\uDE1E"
                                2 -> "Meh \uD83D\uDE10"
                                3 -> "OK \uD83D\uDE42"
                                4 -> "Great! \uD83D\uDE04"
                                5 -> "Perfect! \uD83E\uDD29"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = if (rating == 5) FontWeight.Bold else FontWeight.Normal,
                            fontSize = if (rating == 5) 16.sp else 14.sp
                        )
                    }
                }
            }

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price ($)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Notes / Description") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { save() },
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (entryId == 0L) "Add Burger \uD83C\uDF54" else "Save Changes",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
