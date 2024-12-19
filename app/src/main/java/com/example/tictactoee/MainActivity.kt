package com.example.tictactoee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoee.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold { innerPadding ->
                    TicTacToeApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun TicTacToeApp(
    modifier: Modifier = Modifier
){
    var board by remember { mutableStateOf(Array(3){Array(3){""} })}
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var gameOver by remember { mutableStateOf(false) }

    fun checkWinner(): String? {
        //for rows and columns
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty())
                return board[i][0]
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty())
                return board[0][i]
        }
        // check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty())
            return board[0][0]
        if(board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty())
            return board[0][2]
        return null
    }
    fun isBoardFull(): Boolean {
        return board.all { row -> row.all { cell -> cell.isNotEmpty() } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (gameOver) {
                winner?.let { "Winner: $it" } ?: "It's a Draw!"
            } else {
                "Current Player: $currentPlayer"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        for(i in 0..2){
            Row{
                for(j in 0..2){
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(80.dp)
                            .background(
                                color = if (board[i][j].isEmpty()) Color.LightGray else Color.Cyan,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable ( enabled = !gameOver && board[i][j].isEmpty() ) {
                                if(!gameOver){
                                    winner = checkWinner()
                                    if (!gameOver) {
                                        board[i][j] = currentPlayer
                                        winner = checkWinner()
                                        if (winner != null) {
                                            gameOver = true
                                        } else if (isBoardFull()) {
                                            gameOver = true
                                        } else {
                                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = board[i][j],
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
        if (gameOver) {
            Button(
                onClick = {
                    board = Array(3) { Array(3) { "" } }
                    currentPlayer = "X"
                    winner = null
                    gameOver = false
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ),
                colors = ButtonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(text = "Restart Game")
            }
        }
    }
}

@Preview(showBackground = true,showSystemUi = true)
@Composable
fun PreviewTic(){
    TicTacToeApp()
}