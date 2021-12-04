import java.lang.IllegalStateException

typealias Board = List<MutableList<Int?>>

fun main() {
    fun parseNumbers(input: List<String>) = input.first().splitToSequence(',').map { it.toInt() }

    fun parseBoards(input: List<String>): List<Board> {
        return input
            .asSequence()
            .drop(2)
            .windowed(5, 6)
            .map { board ->
                board.map { line ->
                    line.split(" ")
                        .filter { it.isNotEmpty() }
                        .map { it.toInt() }
                        .toMutableList<Int?>()
                }
            }
            .toList()
    }

    data class Position(val row: Int, val col: Int)

    fun findNumberOnBoard(number: Int, board: Board): Position? {
        for ((rowIndex, row) in board.withIndex()) {
            for ((colIndex, cell) in row.withIndex()) {
                if (cell == number) {
                    return Position(rowIndex, colIndex)
                }
            }
        }
        return null
    }

    fun markNumberOnBoard(position: Position, board: Board) {
        board[position.row][position.col] = null
    }

    fun isRowOrColumnMarked(board: Board, checkPosition: Position): Boolean {
        return board[checkPosition.row].all { cell -> cell == null }
                || board.first().indices.all { row -> board[row][checkPosition.col] == null }
    }

    fun boardUnmarkedSum(board: Board) = board.flatten().sumOf { it ?: 0 }

    fun part1(input: List<String>): Int {
        val boards = parseBoards(input)

        for (number in parseNumbers(input)) {
            for (board in boards) {
                val position = findNumberOnBoard(number, board) ?: continue

                markNumberOnBoard(position, board)

                if (isRowOrColumnMarked(board, position)) {
                    val unmarkedSum = boardUnmarkedSum(board)
                    return unmarkedSum * number
                }
            }
        }

        throw IllegalStateException()
    }

    fun part2(input: List<String>): Int {
        val boards = parseBoards(input)
        val winningBoards = mutableSetOf<Int>()

        for (number in parseNumbers(input)) {
            for ((boardIndex, board) in boards.withIndex()) {
                if (boardIndex in winningBoards) {
                    continue
                }

                val position = findNumberOnBoard(number, board) ?: continue

                markNumberOnBoard(position, board)

                if (isRowOrColumnMarked(board, position)) {
                    if (winningBoards.size < boards.size - 1) {
                        winningBoards.add(boardIndex)
                    } else {
                        val unmarkedSum = boardUnmarkedSum(board)
                        return unmarkedSum * number
                    }
                }
            }
        }

        throw IllegalStateException()
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
