package dev.virunarala.minesweeper.board.data

data class Cell(
    val value: Int = 0,
    val isMine: Boolean = false,
    val isHidden: Boolean = true,
    val isFlagged: Boolean = false
) {
    override fun toString(): String {
        return "Cell{ val:$value, isMine:$isMine, isHidden:$isHidden, isFlagged:$isFlagged}"
    }
}