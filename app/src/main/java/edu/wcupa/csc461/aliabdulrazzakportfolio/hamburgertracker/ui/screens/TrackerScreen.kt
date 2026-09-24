package edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.BurgerType
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.data.Hamburger
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.EntryViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.SortOrder
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.TrackerViewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.components.HamburgerCard
import edu.wcupa.csc461.aliabdulrazzakportfolio.hamburgertracker.ui.theme.BurgerOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    trackerViewModel: TrackerViewModel,
    entryViewModel: EntryViewModel,
    onNavigateToStats: () -> Unit
) {
    val hamburgers by trackerViewModel.hamburgers.collectAsState()
    val uiState by trackerViewModel.uiState.collectAsState()

    var showEntrySheet by remember { mutableStateOf(false) }
    var editingId by remember { mutableLongStateOf(0L) }
    var showFilterPanel by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    // FAB size animation on press (simulated bounce)
    val fabSize by animateDpAsState(
        targetValue = if (showEntrySheet) 52.dp else 56.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "fab_size"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83C\uDF54", fontSize = 22.sp)
                        Text(
                            text = "  Burger Tracker",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { trackerViewModel.toggleFavoritesOnly() }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorites",
                            tint = if (uiState.showFavoritesOnly) Color(0xFFE91E63) else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = { showFilterPanel = !showFilterPanel }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter",
                            tint = if (uiState.selectedType != null || uiState.minRating > 0) BurgerOrange
                            else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                        )
                    }
                    Box {
                        IconButton(onClick = { sortMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort"
                            )
                        }
                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            SortOrder.entries.forEach { order ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = order.label,
                                            fontWeight = if (uiState.sortOrder == order) FontWeight.Bold else FontWeight.Normal,
                                            color = if (uiState.sortOrder == order) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        trackerViewModel.setSortOrder(order)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    IconButton(onClick = onNavigateToStats) {
                        Icon(
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = "Statistics"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingId = 0L
                    showEntrySheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(fabSize)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Burger",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = trackerViewModel::updateSearchQuery,
                placeholder = { Text("Search by name or restaurant...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { trackerViewModel.updateSearchQuery("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filter panel
            AnimatedVisibility(
                visible = showFilterPanel,
                enter = slideInVertically() + fadeIn(),
                exit = fadeOut()
            ) {
                FilterPanel(
                    selectedType = uiState.selectedType,
                    minRating = uiState.minRating,
                    onTypeSelected = trackerViewModel::selectType,
                    onMinRatingChanged = trackerViewModel::setMinRating
                )
            }

            // Active filter chips row
            val hasActiveFilters = uiState.selectedType != null ||
                    uiState.minRating > 0 ||
                    uiState.showFavoritesOnly ||
                    uiState.searchQuery.isNotBlank()
            AnimatedVisibility(visible = hasActiveFilters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${hamburgers.size} result${if (hamburgers.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Burger list
            if (hamburgers.isEmpty()) {
                EmptyState(
                    showingFiltered = hasActiveFilters,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(
                        items = hamburgers,
                        key = { _, h -> h.id }
                    ) { index, hamburger ->
                        AnimatedBurgerItem(
                            hamburger = hamburger,
                            index = index,
                            onEdit = {
                                editingId = it.id
                                showEntrySheet = true
                            },
                            onDelete = trackerViewModel::deleteHamburger,
                            onFavoriteToggle = trackerViewModel::toggleFavorite
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    if (showEntrySheet) {
        EntryBottomSheet(
            entryId = editingId,
            viewModel = entryViewModel,
            onDismiss = { showEntrySheet = false }
        )
    }
}

@Composable
private fun AnimatedBurgerItem(
    hamburger: Hamburger,
    index: Int,
    onEdit: (Hamburger) -> Unit,
    onDelete: (Hamburger) -> Unit,
    onFavoriteToggle: (Hamburger) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(hamburger.id) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            initialOffsetY = { it / 2 }
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
    ) {
        SwipeToDeleteContainer(
            item = hamburger,
            onDelete = onDelete
        ) {
            HamburgerCard(
                hamburger = hamburger,
                onEdit = onEdit,
                onDelete = onDelete,
                onFavoriteToggle = onFavoriteToggle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteContainer(
    item: Hamburger,
    onDelete: (Hamburger) -> Unit,
    content: @Composable () -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd ||
                value == SwipeToDismissBoxValue.EndToStart
            ) {
                onDelete(item)
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            val isSwipingLeft = state.dismissDirection == SwipeToDismissBoxValue.EndToStart
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = if (isSwipingLeft) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Text(
                    text = "\uD83D\uDDD1\uFE0F Delete",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        content = { content() }
    )
}

@Composable
private fun FilterPanel(
    selectedType: BurgerType?,
    minRating: Int,
    onTypeSelected: (BurgerType?) -> Unit,
    onMinRatingChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Filter by Type",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { onTypeSelected(null) },
                    label = { Text("All") }
                )
            }
            items(BurgerType.entries) { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(if (selectedType == type) null else type) },
                    label = { Text("${type.emoji} ${type.label}") }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Minimum Rating",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(0, 1, 2, 3, 4, 5).forEach { stars ->
                FilterChip(
                    selected = minRating == stars,
                    onClick = { onMinRatingChanged(if (minRating == stars) 0 else stars) },
                    label = {
                        Text(
                            if (stars == 0) "Any"
                            else "$stars\u2605"
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    showingFiltered: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "\uD83C\uDF54", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (showingFiltered) "No burgers match your filters" else "No burgers tracked yet!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (showingFiltered) "Try adjusting your search or filters"
            else "Tap + to add your first burger",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}
