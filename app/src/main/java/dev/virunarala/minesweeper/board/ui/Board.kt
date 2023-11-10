package dev.virunarala.minesweeper.board.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import dev.virunarala.minesweeper.board.data.Cell

@Composable
fun Board(
    board: List<List<Cell>>,
    modifier: Modifier = Modifier,
    onCellClick: (cell: Cell, row: Int, col: Int) -> Unit,
    onCellLongClick: (cell: Cell, row: Int, col: Int) -> Unit,) {

    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val screenOrientation = LocalConfiguration.current.orientation
        val columnModifier = when(screenOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Modifier
                    .fillMaxHeight()
            }

            else -> {
                Modifier
            }
        }

        val rowModifier = when(screenOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Modifier
            }

            else -> {
                Modifier
                    .fillMaxWidth()
            }
        }

        Column(modifier = columnModifier
        ) {
            repeat(8) { i ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = if(screenOrientation == Configuration.ORIENTATION_LANDSCAPE) rowModifier.weight(1f) else rowModifier) {
                    repeat(8) { j ->
                        val cell = board[i][j]
                        Cell(board[i][j],
                            Modifier.weight(1f),
                            onCellClick = {
                                onCellClick(it,i,j)
                            },
                            onCellLongClick = {
                                onCellLongClick(it,i,j)
                            }
                        )
                    }
                }

            }
        }
    }
}