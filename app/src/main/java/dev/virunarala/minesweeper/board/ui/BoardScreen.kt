package dev.virunarala.minesweeper.board.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.virunarala.minesweeper.R
import dev.virunarala.minesweeper.board.data.GameState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(
    viewModel: BoardViewModel = viewModel(),
    modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val board = remember { viewModel.board }
    val gameState by remember { viewModel.gameState }
    val totalMines by remember { viewModel.totalMines }

    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center) {
        when(gameState) {
            GameState.NotStarted.id -> {
                Dialog(
                    onDismissRequest = {  }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        OutlinedTextField(
                            value = totalMines,
                            label = { Text(text = stringResource(id = R.string.hint_total_mines)) },
                            onValueChange = { newValue ->
                                viewModel.totalMines.value =newValue
                            },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword,
                                imeAction = ImeAction.Go
                            ),
                            keyboardActions = KeyboardActions (
                                onGo = {
                                    if(viewModel.isInputValid()) {
                                        viewModel.startGame()
                                    } else {
                                        Toast.makeText(context,context.getString(R.string.invalid_mines,1,viewModel.TOTAL_ROWS*viewModel.TOTAL_COLUMNS),Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        )
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                        Button(onClick = {
                            if(viewModel.isInputValid()) {
                                viewModel.startGame()
                            } else {
                                Toast.makeText(context,context.getString(R.string.invalid_mines,1,viewModel.TOTAL_ROWS*viewModel.TOTAL_COLUMNS),Toast.LENGTH_SHORT).show()
                            }

                        }) {
                            Text(text = stringResource(id = R.string.play).uppercase())
                        }
                    }
                }
            }

            GameState.InProgress.id -> {
                // Do nothing
            }

            GameState.GameOver.id -> {
                Dialog(
                    onDismissRequest = {  }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text(text = stringResource(id = R.string.game_over), fontSize = 32.sp)
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                        Button(onClick = { viewModel.restartGame() }) {
                            Text(text = stringResource(id = R.string.play_again).uppercase())
                        }
                    }
                }

            }

            GameState.GameWon.id -> {
                Dialog(
                    onDismissRequest = {  }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text(text = stringResource(id = R.string.game_won), fontSize = 20.sp)
                        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
                        Button(onClick = { viewModel.restartGame() }) {
                            Text(text = stringResource(id = R.string.play_again).uppercase())
                        }
                    }
                }
            }
        }

        Board(
            board,
            onCellClick = { cell, row, col ->
                viewModel.onCellClicked(row,col)
            },
            onCellLongClick = { cell, row, col ->
                viewModel.onCellLongClicked(row,col)
            }
        )
    }
}