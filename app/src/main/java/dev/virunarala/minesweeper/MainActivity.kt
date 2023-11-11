package dev.virunarala.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.virunarala.minesweeper.board.ui.BoardScreen
import dev.virunarala.minesweeper.ui.theme.MinesweeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinesweeperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Minesweeper()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Minesweeper() {

    var shouldShowHelpDialog by remember { mutableStateOf<Boolean>(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name).uppercase()) },
                actions = {
                    IconButton(
                        onClick = {
                            shouldShowHelpDialog = !shouldShowHelpDialog
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_help_24),
                            contentDescription = stringResource(R.string.flag),
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(id = R.color.icon_color_alt)
                        )
                    }
                }
            )
        }
    ) {

        Box {
            BoardScreen(
                modifier = Modifier
                    .padding(it)
            )

            if(shouldShowHelpDialog) {
                AlertDialog(
                    onDismissRequest = { shouldShowHelpDialog = false },
                    title = {
                            Text(text = stringResource(id = R.string.how_to_play),
                                color = MaterialTheme.colorScheme.primary)
                    },
                    text = {
                        Text(text = stringResource(id = R.string.game_help))
                    },
                    confirmButton = {
                        Button(onClick = { shouldShowHelpDialog = false }) {
                            Text(text = stringResource(R.string.ok))
                        }
                    },
                    containerColor = colorResource(id = R.color.surface),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MinesweeperTheme {
        Minesweeper()
    }
}