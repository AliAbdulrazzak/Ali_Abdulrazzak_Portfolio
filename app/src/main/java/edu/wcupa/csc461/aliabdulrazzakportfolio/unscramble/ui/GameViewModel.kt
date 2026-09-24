package edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.data.MAX_NO_OF_WORDS
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.data.SCORE_INCREASE
import edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.data.allWords
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    private val usedWords: MutableSet<String> = mutableSetOf()
    private val revealedIndices: MutableSet<Int> = mutableSetOf()
    private var currentWord = ""
    private var timerJob: Job? = null

    fun selectDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun startGame() {
        timerJob?.cancel()
        usedWords.clear()
        revealedIndices.clear()
        userGuess = ""
        val difficulty = _uiState.value.difficulty
        val scrambled = pickRandomWordAndShuffle(difficulty)
        _uiState.value = GameUiState(
            currentScrambledWord = scrambled,
            difficulty = difficulty,
            timeLeft = difficulty.timeSeconds,
            maxTime = difficulty.timeSeconds,
            gameStarted = true
        )
        startTimer()
    }

    fun backToDifficultyScreen() {
        timerJob?.cancel()
        userGuess = ""
        _uiState.update { it.copy(gameStarted = false, isGameOver = false) }
    }

    fun updateUserGuess(guess: String) {
        userGuess = guess
    }

    fun checkUserGuess() {
        val state = _uiState.value
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val streak = state.streak + 1
            val timeBonus = state.timeLeft / 3
            val streakBonus = if (streak >= 3) 10 else 0
            val hintPenalty = state.hintsUsed * 5
            val pointsEarned = maxOf(5, SCORE_INCREASE + timeBonus + streakBonus - hintPenalty)

            val parts = mutableListOf<String>()
            if (streak >= 3) parts.add("${streak}x Streak +$streakBonus pts")
            if (timeBonus > 0) parts.add("Speed +$timeBonus pts")
            if (hintPenalty > 0) parts.add("Hint -$hintPenalty pts")
            val bonusMessage = parts.joinToString("  |  ")

            usedWords.add(currentWord)
            userGuess = ""
            updateGameState(state.score + pointsEarned, streak, bonusMessage)
        } else {
            _uiState.update { it.copy(isGuessedWordWrong = true, streak = 0, bonusMessage = "") }
            userGuess = ""
        }
    }

    fun skipWord() {
        usedWords.add(currentWord)
        userGuess = ""
        updateGameState(_uiState.value.score, 0, "")
    }

    fun useHint() {
        val state = _uiState.value
        if (state.hintsUsed >= 3) return
        val unrevealed = currentWord.indices.filter { it !in revealedIndices }
        if (unrevealed.isEmpty()) return
        revealedIndices.add(unrevealed.random())
        _uiState.update { it.copy(hintsUsed = it.hintsUsed + 1, hintDisplay = buildHintDisplay()) }
    }

    private fun buildHintDisplay(): String =
        currentWord.indices.joinToString(" ") { i ->
            if (i in revealedIndices) currentWord[i].uppercase() else "_"
        }

    private fun updateGameState(updatedScore: Int, newStreak: Int, bonusMessage: String) {
        timerJob?.cancel()
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update {
                it.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true,
                    streak = newStreak,
                    bonusMessage = bonusMessage
                )
            }
        } else {
            val difficulty = _uiState.value.difficulty
            revealedIndices.clear()
            val scrambled = pickRandomWordAndShuffle(difficulty)
            _uiState.update { state ->
                state.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = scrambled,
                    currentWordCount = state.currentWordCount + 1,
                    score = updatedScore,
                    streak = newStreak,
                    timeLeft = difficulty.timeSeconds,
                    maxTime = difficulty.timeSeconds,
                    hintsUsed = 0,
                    hintDisplay = "",
                    bonusMessage = bonusMessage
                )
            }
            startTimer()
        }
    }

    private fun pickRandomWordAndShuffle(difficulty: Difficulty): String {
        val pool = when (difficulty) {
            Difficulty.EASY -> allWords.filter { it.length <= 5 }
            Difficulty.MEDIUM -> allWords.filter { it.length in 6..8 }
            Difficulty.HARD -> allWords.filter { it.length >= 9 }
        }.toSet()

        val available = (pool - usedWords).ifEmpty { allWords - usedWords }.ifEmpty { allWords }
        currentWord = available.random()
        return shuffleWord(currentWord)
    }

    private fun shuffleWord(word: String): String {
        if (word.length <= 1) return word
        val chars = word.toCharArray()
        do { chars.shuffle() } while (String(chars) == word)
        return String(chars)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val current = _uiState.value
                if (!current.gameStarted || current.isGameOver) break
                val newTime = current.timeLeft - 1
                _uiState.update { it.copy(timeLeft = newTime) }
                if (newTime <= 0) {
                    usedWords.add(currentWord)
                    updateGameState(_uiState.value.score, 0, "Time's up!")
                    break
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
