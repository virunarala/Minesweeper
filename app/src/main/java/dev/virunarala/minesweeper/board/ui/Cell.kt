package dev.virunarala.minesweeper.board.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.virunarala.minesweeper.R
import dev.virunarala.minesweeper.board.data.Cell

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cell(cell: Cell,
         modifier: Modifier = Modifier,
         onCellClick: (cell: Cell) -> Unit,
         onCellLongClick: (cell: Cell) -> Unit
) {

    val edgeOffset = 8f
    val rightEdgeColor = Color.Gray
    val bottomEdgeColor = Color.Gray
    val shouldDrawBorder = cell.isHidden

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevationOffset by remember {
        derivedStateOf {
            if(isPressed) {
                IntOffset(edgeOffset.toInt(),edgeOffset.toInt())
            } else {
                IntOffset.Zero
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(1.5.dp)
            .background(color = Color.LightGray.copy(alpha = 0.4f))
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onCellClick(cell) },
                onLongClick = { onCellLongClick(cell) }
            )
            .drawBehind {
                if (shouldDrawBorder && !isPressed) {
                    val rightEdge = Path().apply {
                        moveTo(size.width, 0f)
                        lineTo(size.width + edgeOffset, edgeOffset)
                        lineTo(size.width + edgeOffset, size.height + edgeOffset)
                        lineTo(size.width, size.height)
                        close()
                    }

                    val bottomEdge = Path().apply {
                        moveTo(size.width, size.height)
                        lineTo(size.width + edgeOffset, size.height + edgeOffset)
                        lineTo(edgeOffset, size.height + edgeOffset)
                        lineTo(0f, size.height)
                        close()
                    }

                    drawPath(path = rightEdge, color = rightEdgeColor, style = Fill)
                    drawPath(path = bottomEdge, color = bottomEdgeColor, style = Fill)
                }
            }

    ) {

        Box(modifier = Modifier
            .offset {
                elevationOffset
            }) {
            if(cell.isHidden) {
                if(cell.isFlagged) {
                    Image(painter = painterResource(id = R.drawable.flag_triangle),
                        contentDescription = stringResource(R.string.flag),
                        modifier = Modifier.size(24.dp)
                    )
                }

            } else {

                if(cell.isMine) {
                    Image(painter = painterResource(id = R.drawable.bomb),
                        contentDescription = stringResource(R.string.mine),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    if(cell.value==0) {

                    } else {
                        Text(text = cell.value.toString(),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CellPreview() {
    Cell(
        cell = Cell(
            value = 1,
            isMine = false,
            isHidden = false,
            isFlagged = false
        ),
        onCellClick = {  },
        onCellLongClick = {  }
    )
}