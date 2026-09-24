package edu.wcupa.csc461.aliabdulrazzakportfolio.unscramble.ui

data class GameUiState(
    val currentScrambledWord: String = "",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val streak: Int = 0,
    val timeLeft: Int = 30,
    val maxTime: Int = 30,
    val hintsUsed: Int = 0,
    val hintDisplay: String = "",
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val bonusMessage: String = "",
    val gameStarted: Boolean = false
)

enum class Difficulty(val label: String, val timeSeconds: Int, val description: String) {
    EASY("Easy", 45, "Short words • 45s per word"),
    MEDIUM("Medium", 30, "Medium words • 30s per word"),
    HARD("Hard", 20, "Long words • 20s per word")
}
