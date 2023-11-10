package dev.virunarala.minesweeper.board.data

enum class GameState(val id: Int) {
    NotStarted(0),
    InProgress(1),
    GameOver(2),
    GameWon(3)
}