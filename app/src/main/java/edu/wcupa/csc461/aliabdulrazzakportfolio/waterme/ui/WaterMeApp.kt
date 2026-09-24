package edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.R
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.FIVE_SECONDS
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ONE_DAY
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.SEVEN_DAYS
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.THIRTY_DAYS
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.data.Reminder
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.model.Plant
import edu.wcupa.csc461.aliabdulrazzakportfolio.waterme.ui.theme.WaterMeTheme
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterMeApp(waterViewModel: WaterViewModel = viewModel(factory = WaterViewModel.Factory)) {
    WaterMeTheme(darkTheme = waterViewModel.darkTheme) {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var showAddPlant by rememberSaveable { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.waterme_app_name),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(
                                    R.string.waterme_watering_streak,
                                    waterViewModel.totalWaterings
                                ),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showAddPlant = true }) {
                            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.waterme_add_plant))
                        }
                        IconButton(onClick = { waterViewModel.toggleTheme() }) {
                            Text(
                                text = if (waterViewModel.darkTheme) "☀️" else "🌙",
                                fontSize = 20.sp
                            )
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                PlantListContent(
                    waterViewModel = waterViewModel,
                    onScheduleReminder = { reminder, plantName ->
                        waterViewModel.scheduleReminder(reminder)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Reminder set for $plantName"
                            )
                        }
                    },
                    onCancelReminder = { plantName ->
                        waterViewModel.cancelReminder(plantName)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Reminder cancelled"
                            )
                        }
                    },
                    onMarkWatered = { plant, plantName ->
                        waterViewModel.markWatered(plant)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Nice — $plantName is happy!"
                            )
                        }
                    }
                )
            }
        }

        if (showAddPlant) {
            AddPlantDialog(
                onDismiss = { showAddPlant = false },
                onSave = { plant ->
                    waterViewModel.addCustomPlant(plant)
                    showAddPlant = false
                }
            )
        }
    }
}

@Composable
fun PlantListContent(
    waterViewModel: WaterViewModel,
    onScheduleReminder: (Reminder, String) -> Unit,
    onCancelReminder: (String) -> Unit,
    onMarkWatered: (Plant, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var reminderForPlant by rememberSaveable { mutableStateOf<Plant?>(null) }
    var careForPlant by rememberSaveable { mutableStateOf<Plant?>(null) }
    var customReminderForPlant by rememberSaveable { mutableStateOf<Plant?>(null) }

    val plants = waterViewModel.visiblePlants

    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        item {
            FilterChipsRow(
                allTypes = waterViewModel.availableTypes(),
                selected = waterViewModel.selectedFilter,
                onSelect = { waterViewModel.selectedFilter = it }
            )
        }
        items(items = plants, key = { it.id }) { plant ->
            val resolvedName = plant.customName ?: stringResource(plant.name)
            PlantListItem(
                plant = plant,
                resolvedName = resolvedName,
                lastWateredMs = waterViewModel.lastWatered(plant.id),
                onSetReminder = { reminderForPlant = plant },
                onCancelReminder = { onCancelReminder(resolvedName) },
                onMarkWatered = { onMarkWatered(plant, resolvedName) },
                onShowCare = { careForPlant = plant },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    reminderForPlant?.let { plant ->
        val resolvedName = plant.customName ?: stringResource(plant.name)
        ReminderDialogContent(
            onDialogDismiss = { reminderForPlant = null },
            plantName = resolvedName,
            onScheduleReminder = { onScheduleReminder(it, resolvedName) },
            onCustomRequested = {
                reminderForPlant = null
                customReminderForPlant = plant
            }
        )
    }

    customReminderForPlant?.let { plant ->
        val resolvedName = plant.customName ?: stringResource(plant.name)
        CustomReminderDialog(
            plantName = resolvedName,
            onDismiss = { customReminderForPlant = null },
            onConfirm = { minutes ->
                onScheduleReminder(
                    Reminder(R.string.waterme_custom_duration, minutes, TimeUnit.MINUTES, resolvedName),
                    resolvedName
                )
                customReminderForPlant = null
            }
        )
    }

    careForPlant?.let { plant ->
        CareTipsDialog(plant = plant, onDismiss = { careForPlant = null })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipsRow(
    allTypes: List<Int>,
    selected: Int?,
    onSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelect(null) },
                label = { Text(stringResource(R.string.waterme_filter_all)) }
            )
        }
        items(items = allTypes, key = { it }) { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onSelect(type) },
                label = { Text(stringResource(type)) }
            )
        }
    }
}

@Composable
fun PlantListItem(
    plant: Plant,
    resolvedName: String,
    lastWateredMs: Long?,
    onSetReminder: () -> Unit,
    onCancelReminder: () -> Unit,
    onMarkWatered: () -> Unit,
    onShowCare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = plant.emoji, fontSize = 36.sp)
                Spacer(Modifier.size(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resolvedName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(plant.type),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Text(
                text = stringResource(plant.description),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${stringResource(R.string.waterme_water)} ${stringResource(plant.schedule)}",
                style = MaterialTheme.typography.bodyMedium
            )

            ThirstIndicator(
                lastWateredMs = lastWateredMs,
                intervalDays = plant.recommendedIntervalDays
            )

            Spacer(Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(
                    onClick = onMarkWatered,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.waterme_water_now), maxLines = 1)
                }
                OutlinedButton(
                    onClick = onShowCare,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.waterme_care_tips), maxLines = 1)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(
                    onClick = onSetReminder,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.waterme_set_reminder), maxLines = 1)
                }
                OutlinedButton(
                    onClick = onCancelReminder,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.waterme_cancel_reminder), maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun ThirstIndicator(
    lastWateredMs: Long?,
    intervalDays: Int,
    modifier: Modifier = Modifier
) {
    val now = System.currentTimeMillis()
    val intervalMs = intervalDays.coerceAtLeast(1).toLong() * 24L * 60L * 60L * 1000L
    val elapsed = if (lastWateredMs == null) intervalMs else (now - lastWateredMs).coerceAtLeast(0)
    val thirst = (elapsed.toFloat() / intervalMs.toFloat()).coerceIn(0f, 1f)

    val statusText = when {
        lastWateredMs == null -> stringResource(R.string.waterme_last_watered_never)
        else -> {
            val diffMin = ((now - lastWateredMs) / 60_000L).toInt()
            when {
                diffMin < 1 -> stringResource(R.string.waterme_last_watered_just_now)
                diffMin < 60 -> stringResource(R.string.waterme_last_watered_minutes, diffMin)
                diffMin < 60 * 24 -> stringResource(R.string.waterme_last_watered_hours, diffMin / 60)
                else -> stringResource(R.string.waterme_last_watered_days, diffMin / (60 * 24))
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.waterme_thirst_label),
                style = MaterialTheme.typography.labelMedium
            )
            Text(text = statusText, style = MaterialTheme.typography.labelMedium)
        }
        LinearProgressIndicator(
            progress = { thirst },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
fun ReminderDialogContent(
    onDialogDismiss: () -> Unit,
    plantName: String,
    onScheduleReminder: (Reminder) -> Unit,
    onCustomRequested: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reminders = listOf(
        Reminder(R.string.waterme_five_seconds, FIVE_SECONDS, TimeUnit.SECONDS, plantName),
        Reminder(R.string.waterme_one_day, ONE_DAY, TimeUnit.DAYS, plantName),
        Reminder(R.string.waterme_one_week, SEVEN_DAYS, TimeUnit.DAYS, plantName),
        Reminder(R.string.waterme_one_month, THIRTY_DAYS, TimeUnit.DAYS, plantName)
    )

    AlertDialog(
        onDismissRequest = onDialogDismiss,
        confirmButton = {
            TextButton(onClick = onCustomRequested) {
                Text(stringResource(R.string.waterme_custom_duration))
            }
        },
        dismissButton = {
            TextButton(onClick = onDialogDismiss) {
                Text(stringResource(R.string.waterme_close))
            }
        },
        title = { Text(stringResource(R.string.waterme_remind_me, plantName)) },
        text = {
            Column {
                reminders.forEach { reminder ->
                    Text(
                        text = stringResource(reminder.durationRes),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onScheduleReminder(reminder)
                                onDialogDismiss()
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun CustomReminderDialog(
    plantName: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var minutesText by rememberSaveable { mutableStateOf("30") }
    val minutes = minutesText.toLongOrNull()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { minutes?.let { onConfirm(it) } },
                enabled = minutes != null && minutes > 0
            ) {
                Text(stringResource(R.string.waterme_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.waterme_cancel))
            }
        },
        title = { Text(stringResource(R.string.waterme_custom_reminder_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.waterme_remind_me, plantName))
                OutlinedTextField(
                    value = minutesText,
                    onValueChange = { minutesText = it.filter(Char::isDigit).take(5) },
                    label = { Text(stringResource(R.string.waterme_minutes_from_now)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    )
}

@Composable
fun CareTipsDialog(plant: Plant, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.waterme_close))
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = plant.emoji, fontSize = 28.sp)
                Spacer(Modifier.size(8.dp))
                Text(
                    text = stringResource(
                        R.string.waterme_care_tips_for,
                        plant.customName ?: stringResource(plant.name)
                    )
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CareRow(
                    label = stringResource(R.string.waterme_care_sunlight),
                    value = stringResource(plant.sunlight)
                )
                CareRow(
                    label = stringResource(R.string.waterme_care_soil),
                    value = stringResource(plant.soil)
                )
                CareRow(
                    label = stringResource(R.string.waterme_care_schedule),
                    value = stringResource(R.string.waterme_every_n_days, plant.recommendedIntervalDays)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.waterme_care_fact),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = stringResource(plant.funFact),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

@Composable
private fun CareRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            modifier = Modifier.weight(1.4f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AddPlantDialog(onDismiss: () -> Unit, onSave: (Plant) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var emoji by rememberSaveable { mutableStateOf("🌿") }
    var intervalText by rememberSaveable { mutableStateOf("3") }
    val interval = intervalText.toIntOrNull()
    val canSave = name.isNotBlank() && interval != null && interval > 0 && emoji.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = {
                    val days = interval ?: 1
                    val schedule = when {
                        days <= 1 -> R.string.waterme_daily
                        days in 2..13 -> R.string.waterme_weekly
                        else -> R.string.waterme_monthly
                    }
                    onSave(
                        Plant(
                            id = "custom-${UUID.randomUUID()}",
                            name = R.string.waterme_app_name,
                            type = R.string.waterme_custom_type,
                            description = R.string.waterme_user_added_plant,
                            schedule = schedule,
                            emoji = emoji,
                            recommendedIntervalDays = days,
                            sunlight = R.string.waterme_sun_indirect,
                            soil = R.string.waterme_soil_default,
                            funFact = R.string.waterme_fact_default,
                            customName = name.trim()
                        )
                    )
                }
            ) { Text(stringResource(R.string.waterme_save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.waterme_cancel)) }
        },
        title = { Text(stringResource(R.string.waterme_add_custom_plant)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.take(40) },
                    label = { Text(stringResource(R.string.waterme_plant_name_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it.take(4) },
                    label = { Text(stringResource(R.string.waterme_emoji_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = intervalText,
                    onValueChange = { intervalText = it.filter(Char::isDigit).take(3) },
                    label = { Text(stringResource(R.string.waterme_interval_days_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    )
}
