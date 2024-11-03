/*
 * Tic Tac Toe with Machine
 * Developer: Rahil N. Shaikh
 * Email: rns54@georgetown.edu
 *
 * References:
 * - GeeksforGeeks for Minimax Algorithm
 * - Figma for Color Schemes
 * - ChatGPT for Layout Help
 * - Adobe Illustrator for Logo and Graphic Designs
 */

package com.example.tictactoe

import androidx.compose.ui.res.painterResource
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                var currentScreen by remember { mutableStateOf("Home") }
                var isHumanFirst by remember { mutableStateOf(true) }

                when (currentScreen) {
                    "Home" -> {
                        HomePage(
                            onHumanFirstClicked = {
                                isHumanFirst = true
                                currentScreen = "Game"
                            },
                            onMachineFirstClicked = {
                                isHumanFirst = false
                                currentScreen = "Game"
                            }
                        )
                    }

                    "Game" -> {
                        GamePage(
                            isHumanFirst = isHumanFirst,
                            onPlayAgain = {
                                currentScreen = "Home"
                            }
                        )
                    }
                }
            }
        }
    }
}

// Define the Move data class
data class Move(val row: Int, val col: Int)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    onHumanFirstClicked: () -> Unit,
    onMachineFirstClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tic Tac Toe with Machine",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults
                    .topAppBarColors(Color(0xff3b50bc)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .statusBarsPadding()
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xff8689f5))
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier.width(250.dp)
                    )


                    // "You Play First" Button
                    Button(
                        onClick = onHumanFirstClicked,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xff4747ff)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text(
                            "HUMAN PLAYS FIRST",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // "Machine Plays First" Button
                    Button(
                        onClick = onMachineFirstClicked,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(0xff4747ff)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text(
                            "MACHINE PLAYS FIRST",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
}


// Updated GamePage with AI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePage(
    isHumanFirst: Boolean,
    onPlayAgain: () -> Unit,
) {
    val firstPlayerSymbol = "X"
    val secondPlayerSymbol = "O"

    val humanSymbol = if (isHumanFirst) firstPlayerSymbol else secondPlayerSymbol
    val machineSymbol = if (isHumanFirst) secondPlayerSymbol else firstPlayerSymbol

    var currentPlayer by remember { mutableStateOf(firstPlayerSymbol) }
    var gameResult by remember { mutableStateOf<String?>(null) }
    var winningCells by remember { mutableStateOf<List<Pair<Int, Int>>?>(null) }

    val boardState = remember {
        mutableStateListOf(
            mutableStateListOf("", "", ""),
            mutableStateListOf("", "", ""),
            mutableStateListOf("", "", "")
        )
    }

    // Machine makes a move when it's its turn
    LaunchedEffect(currentPlayer) {
        if (currentPlayer == machineSymbol && gameResult == null) {
            machineMakeMove(boardState, machineSymbol, humanSymbol)
            val winningPositions = checkWinner(boardState, machineSymbol)
            if (winningPositions != null) {
                winningCells = winningPositions
                gameResult = "MACHINE WINS!"
            } else if (isBoardFull(boardState)) {
                gameResult = "IT'S A DRAW!"
            } else {
                currentPlayer = humanSymbol
            }
        }
    }

    // Top App Bar
    TopAppBar(
        title = {
            Text(
                "Tic Tac Toe with Machine",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        },
        colors = TopAppBarDefaults
            .topAppBarColors(Color(0xff3b50bc)),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .statusBarsPadding()
    )
    // Main Content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp) // Adjust for TopAppBar height
            .background(Color(0xff0161cd)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Display Game Result or Current Player
        // Display Game Result or Current Player in a Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xff4747ff)),
            elevation = CardDefaults.cardElevation(defaultElevation = 70.dp),
            shape = RectangleShape
        ) {

            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (gameResult != null) {
                    Text(
                        text = gameResult!!,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xffccff00),
                        textAlign = TextAlign.Center
                    )
                } else {
                    val playerTurn =
                        if (currentPlayer == humanSymbol) "YOUR TURN ($humanSymbol)" else "MACHINE'S TURN ($machineSymbol)"
                    Text(
                        text = playerTurn,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xffccff00)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(25.dp))

        // Grid
        // Grid
        for (rowIndex in 0..2) {
            Row {
                for (colIndex in 0..2) {
                    val cellContent = boardState[rowIndex][colIndex]
                    val isHighlighted = winningCells?.contains(Pair(rowIndex, colIndex)) ?: false
                    val isCellEnabled =
                        cellContent.isEmpty() && gameResult == null && currentPlayer == humanSymbol

                    Cell(
                        content = cellContent,
                        onClick = {
                            if (isCellEnabled) {
                                boardState[rowIndex][colIndex] = humanSymbol
                                val winningPositions = checkWinner(boardState, humanSymbol)
                                if (winningPositions != null) {
                                    winningCells = winningPositions
                                    gameResult = "YOU WIN!"
                                } else if (isBoardFull(boardState)) {
                                    gameResult = "IT'S A DRAW!"
                                } else {
                                    currentPlayer = machineSymbol
                                }
                            }
                        },
                        highlight = isHighlighted,
                        enabled = isCellEnabled  // Include this parameter
                    )
                }
            }
        }


        // "Play Again" Button
        if (gameResult != null) {
            Button(
                onClick = {
                    onPlayAgain()
                },
                modifier = Modifier.padding(top = 16.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Color(0xff800040))
            ) {
                Text(
                    "Play Again",
                    color = Color.White
                )
            }
        }
    }
}


// Implement the machine's AI
fun machineMakeMove(
    board: List<MutableList<String>>,
    machineSymbol: String,
    humanSymbol: String,
) {
    val bestMove = findBestMove(board, machineSymbol, humanSymbol)
    if (bestMove != null) {
        board[bestMove.row][bestMove.col] = machineSymbol
    }
}


fun findBestMove(
    board: List<MutableList<String>>,
    machineSymbol: String,
    humanSymbol: String,
): Move? {
    var bestScore = Int.MIN_VALUE
    var bestMove: Move? = null

    for (row in 0..2) {
        for (col in 0..2) {
            if (board[row][col] == "") {
                // Try the move
                board[row][col] = machineSymbol
                val score = minimax(board, 0, false, machineSymbol, humanSymbol)
                // Undo the move
                board[row][col] = ""
                if (score > bestScore) {
                    bestScore = score
                    bestMove = Move(row, col)
                }
            }
        }
    }
    return bestMove
}


fun minimax(
    board: List<MutableList<String>>,
    depth: Int,
    isMaximizing: Boolean,
    machineSymbol: String,
    humanSymbol: String,
): Int {
    val machineWin = checkWinner(board, machineSymbol)
    val humanWin = checkWinner(board, humanSymbol)

    if (machineWin != null) {
        return 10 - depth
    }
    if (humanWin != null) {
        return depth - 10
    }
    if (isBoardFull(board)) {
        return 0
    }

    if (isMaximizing) {
        var bestScore = Int.MIN_VALUE
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == "") {
                    board[row][col] = machineSymbol
                    val score = minimax(board, depth + 1, false, machineSymbol, humanSymbol)
                    board[row][col] = ""
                    bestScore = maxOf(score, bestScore)
                }
            }
        }
        return bestScore
    } else {
        var bestScore = Int.MAX_VALUE
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == "") {
                    board[row][col] = humanSymbol
                    val score = minimax(board, depth + 1, true, machineSymbol, humanSymbol)
                    board[row][col] = ""
                    bestScore = minOf(score, bestScore)
                }
            }
        }
        return bestScore
    }
}


fun checkWinner(board: List<List<String>>, player: String): List<Pair<Int, Int>>? {
    // Check Rows
    for (row in 0..2) {
        if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
            return listOf(
                Pair(row, 0),
                Pair(row, 1),
                Pair(row, 2)
            )
        }
    }
    // Check Columns
    for (col in 0..2) {
        if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
            return listOf(
                Pair(0, col),
                Pair(1, col),
                Pair(2, col)
            )
        }
    }
    // Check Diagonals
    if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
        return listOf(
            Pair(0, 0),
            Pair(1, 1),
            Pair(2, 2)
        )
    }
    if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
        return listOf(
            Pair(0, 2),
            Pair(1, 1),
            Pair(2, 0)
        )
    }
    return null
}


fun isBoardFull(board: List<List<String>>): Boolean {
    for (row in board) {
        if (row.contains("")) {
            return false
        }
    }
    return true
}


@Composable
fun Cell(
    content: String,
    onClick: () -> Unit,
    highlight: Boolean,
    enabled: Boolean,
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(3.dp) // Add padding around each cell
            .border(1.dp, Color.Black)
            .background(if (highlight) Color(0xffc9d5ff) else Color(0xff0a033c))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when (content) {
            "X" -> {
                Image(
                    painter = painterResource(id = R.drawable.x),
                    contentDescription = "X",
                    modifier = Modifier.fillMaxSize()
                )
            }

            "O" -> {
                Image(
                    painter = painterResource(id = R.drawable.o),
                    contentDescription = "O",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    TicTacToeTheme {
        HomePage(
            onHumanFirstClicked = {},
            onMachineFirstClicked = {}
        )
    }
}