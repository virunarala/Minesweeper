package dev.virunarala.minesweeper.board.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dev.virunarala.minesweeper.board.data.Cell
import dev.virunarala.minesweeper.board.data.GameState

class BoardViewModel: ViewModel() {

    companion object {
        private const val TAG = "BoardViewModel"
    }

    val TOTAL_ROWS = 8
    val TOTAL_COLUMNS = 8

    var gameState = mutableStateOf<Int>(GameState.NotStarted.id)
    var totalMines = mutableStateOf<String>("10")
    val board = mutableStateListOf<SnapshotStateList<Cell>>()
    var totalCellsRevealed = 0

    init {
        initBoard()
    }

    private fun initBoard() {
        repeat(TOTAL_ROWS) { rowIndex ->
            val row = mutableListOf<Cell>()
            repeat(TOTAL_COLUMNS) { colIndex ->
                row.add(colIndex,Cell())
            }
            board.add(rowIndex,row.toMutableStateList())
        }
    }

    private fun addMines(totalMines: Int) {
        var numberOfMines = 0
        while(numberOfMines < totalMines) {
            val row = (0 until TOTAL_ROWS).random()
            val col = (0 until TOTAL_COLUMNS).random()
            val cell = board[row][col]
            if(cell.isMine) {
                // Do nothing
            } else {
                board[row][col] = cell.copy(isMine = true)
                numberOfMines++
            }
        }
    }

    private fun gameOver() {
        revealAllMines()
        gameState.value = GameState.GameOver.id
    }

    private fun revealAllMines() {
        repeat(TOTAL_ROWS) { row ->
            repeat(TOTAL_COLUMNS) { col ->
                val cell = board[row][col]
                if(cell.isMine) {
                    board[row][col] = cell.copy(isHidden = false)
                }
            }
        }
    }

    fun onCellClicked(rowIndex: Int, colIndex: Int) {
        val cell = board[rowIndex][colIndex]
        if(gameState.value!=GameState.InProgress.id || !cell.isHidden || cell.isFlagged) {
            //Do nothing
        } else {
            revealCells(rowIndex,colIndex)
        }
    }

    fun onCellLongClicked(rowIndex: Int, colIndex: Int) {
        if(gameState.value!=GameState.InProgress.id) {
            //Do nothing
        } else {
            val cell = board[rowIndex][colIndex]
            board[rowIndex][colIndex] = cell.copy(isFlagged = !cell.isFlagged)
        }
    }

    private fun revealCells(rowIndex: Int, colIndex: Int) {
        if(rowIndex<0 || rowIndex>=TOTAL_ROWS || colIndex<0 || colIndex>=TOTAL_COLUMNS) {
            return
        }

        val cell = board[rowIndex][colIndex]

        if(!cell.isHidden) {
            return
        }

        val mineCount = getMineCount(rowIndex,colIndex)
        board[rowIndex][colIndex] = cell.copy(value = mineCount, isHidden = false)
        totalCellsRevealed++

        if(cell.isMine) {
            gameOver()
            return
        } else if(isGameWon()) {
            gameState.value = GameState.GameWon.id
            return
        }

        if(mineCount==0) {
            // Blank Cell. Recursively reveal surrounding cells
            for(r in rowIndex-1..rowIndex+1) {
                for(c in colIndex-1..colIndex+1) {
                    revealCells(r,c)
                }
            }
        }
    }

    private fun isMineAt(rowIndex: Int, colIndex: Int): Boolean {
        if(rowIndex<0 || rowIndex>=TOTAL_ROWS || colIndex<0 || colIndex>=TOTAL_COLUMNS) {
            return false
        }
        val cell = board[rowIndex][colIndex]
        return cell.isMine
    }

    private fun getMineCount(rowIndex: Int, colIndex: Int): Int {
        var mineCount = 0

        for(r in rowIndex-1..rowIndex+1) {
            for(c in colIndex-1..colIndex+1) {
                if(isMineAt(r,c)) {
                    mineCount++
                }
            }
        }
        return mineCount
    }

    private fun isGameWon(): Boolean {
        return totalCellsRevealed == (TOTAL_ROWS * TOTAL_COLUMNS - totalMines.value.toInt())
    }

    fun startGame() {
        board.clear()
        initBoard()
        totalCellsRevealed = 0
        addMines(totalMines.value.toInt())
        gameState.value = GameState.InProgress.id
    }

    fun restartGame() {
        gameState.value = GameState.NotStarted.id
    }

    fun isInputValid(): Boolean {
        try {
            if(totalMines.value.isBlank()) {
                return false
            }
            val mines = totalMines.value.toInt()
            if(mines<=0 || mines>= TOTAL_ROWS * TOTAL_COLUMNS) {
                return false
            } else {
                return true
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }
}