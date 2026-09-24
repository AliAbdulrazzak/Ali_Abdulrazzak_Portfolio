package edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.wcupa.csc461.aliabdulrazzakportfolio.R
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui.theme.UnscrambleTheme

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    if (!gameUiState.gameStarted) {
        DifficultyScreen(
            selectedDifficulty = gameUiState.difficulty,
            onDifficultySelect = { gameViewModel.selectDifficulty(it) },
            onStartGame = { gameViewModel.startGame() }
        )
    } else {
        ActiveGameScreen(gameViewModel = gameViewModel, gameUiState = gameUiState)
    }

    if (gameUiState.isGameOver) {
        FinalScoreDialog(
            score = gameUiState.score,
            onPlayAgain = { gameViewModel.startGame() },
            onNewGame = { gameViewModel.backToDifficultyScreen() }
        )
    }
}

@Composable
private fun DifficultyScreen(
    selectedDifficulty: Difficulty,
    onDifficultySelect: (Difficulty) -> Unit,
    onStartGame: () -> Unit
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .safeDrawingPadding()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.unscramble_app_name),
            style = typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.unscramble_select_difficulty),
            style = typography.titleMedium,
            textAlign = TextAlign.Center,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        Difficulty.entries.forEach { difficulty ->
            val isSelected = difficulty == selectedDifficulty
            if (isSelected) {
                Button(
                    onClick = { onDifficultySelect(difficulty) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = difficulty.label, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = difficulty.description, fontSize = 13.sp)
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { onDifficultySelect(difficulty) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = difficulty.label, fontSize = 18.sp)
                        Text(text = difficulty.description, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onStartGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = stringResource(R.string.unscramble_start_game), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ActiveGameScreen(
    gameViewModel: GameViewModel,
    gameUiState: GameUiState
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.unscramble_app_name),
                style = typography.titleLarge,
            )
            if (gameUiState.streak >= 2) {
                Surface(
                    color = colorScheme.tertiary,
                    shape = shapes.small
                ) {
                    Text(
                        text = "${gameUiState.streak}x Streak",
                        style = typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onTertiary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TimerBar(
            timeLeft = gameUiState.timeLeft,
            maxTime = gameUiState.maxTime,
            modifier = Modifier.fillMaxWidth()
        )

        if (gameUiState.bonusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = gameUiState.bonusMessage,
                style = typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.tertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        GameLayout(
            currentScrambledWord = gameUiState.currentScrambledWord,
            currentWordCount = gameUiState.currentWordCount,
            isGuessWrong = gameUiState.isGuessedWordWrong,
            userGuess = gameViewModel.userGuess,
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            hintDisplay = gameUiState.hintDisplay,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { gameViewModel.checkUserGuess() }
            ) {
                Text(text = stringResource(R.string.unscramble_submit), fontSize = 16.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { gameViewModel.skipWord() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.unscramble_skip), fontSize = 16.sp)
                }
                val hintsLeft = 3 - gameUiState.hintsUsed
                OutlinedButton(
                    onClick = { gameViewModel.useHint() },
                    enabled = hintsLeft > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.unscramble_hint, hintsLeft),
                        fontSize = 16.sp
                    )
                }
            }
        }

        GameStatus(score = gameUiState.score, modifier = Modifier.padding(20.dp))
    }
}

@Composable
private fun TimerBar(timeLeft: Int, maxTime: Int, modifier: Modifier = Modifier) {
    val progress = if (maxTime > 0) timeLeft.toFloat() / maxTime else 0f
    val timerColor = when {
        progress > 0.5f -> colorScheme.primary
        progress > 0.25f -> Color(0xFFE65100)
        else -> colorScheme.error
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.unscramble_time_left),
                style = typography.labelMedium,
                color = colorScheme.onSurfaceVariant
            )
            Text(
                text = "${timeLeft}s",
                style = typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = timerColor
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = timerColor,
            trackColor = colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun GameLayout(
    currentScrambledWord: String,
    currentWordCount: Int,
    isGuessWrong: Boolean,
    userGuess: String,
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    hintDisplay: String,
    modifier: Modifier = Modifier
) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = stringResource(R.string.unscramble_word_count, currentWordCount),
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )

            Text(
                text = currentScrambledWord,
                style = typography.displayMedium
            )

            if (hintDisplay.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.unscramble_hint_label, hintDisplay),
                    style = typography.titleMedium,
                    color = colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = stringResource(R.string.unscramble_instructions),
                textAlign = TextAlign.Center,
                style = typography.titleMedium
            )
            OutlinedTextField(
                value = userGuess,
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                onValueChange = onUserGuessChanged,
                label = {
                    if (isGuessWrong) {
                        Text(stringResource(R.string.unscramble_wrong_guess))
                    } else {
                        Text(stringResource(R.string.unscramble_enter_your_word))
                    }
                },
                isError = isGuessWrong,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onKeyboardDone() })
            )
        }
    }
}

@Composable
private fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Text(
            text = stringResource(R.string.unscramble_score, score),
            style = typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    onNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.unscramble_congratulations)) },
        text = { Text(text = stringResource(R.string.unscramble_you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { activity.finish() }) {
                Text(text = stringResource(R.string.unscramble_exit))
            }
        },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onPlayAgain) {
                    Text(text = stringResource(R.string.unscramble_play_again))
                }
                TextButton(onClick = onNewGame) {
                    Text(text = stringResource(R.string.unscramble_new_game))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    UnscrambleTheme {
        GameScreen()
    }
}
