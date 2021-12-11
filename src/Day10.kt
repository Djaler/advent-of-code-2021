import java.util.*

fun main() {
    val mapOpenToCloseBracket = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    fun findUnexpectedBracket(line: String): Char? {
        val stack = ArrayDeque<Char>()
        for (char in line) {
            if (char in mapOpenToCloseBracket.keys) {
                stack.add(char)
            } else {
                val last: Char? = stack.peekLast()
                if (last == null || mapOpenToCloseBracket.getValue(last) != char) {
                    return char
                } else {
                    stack.pollLast()
                }
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        val mapBracketToPoints = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
        return input
            .asSequence()
            .map { line -> findUnexpectedBracket(line) }
            .sumOf { char -> char?.let { mapBracketToPoints[it] } ?: 0 }
    }

    fun completeLine(line: String): String? {
        val stack = ArrayDeque<Char>()
        for (char in line) {
            if (char in mapOpenToCloseBracket.keys) {
                stack.add(char)
            } else {
                val last: Char? = stack.peekLast()
                if (last == null || mapOpenToCloseBracket.getValue(last) != char) {
                    return null
                } else {
                    stack.pollLast()
                }
            }
        }

        if (stack.isEmpty()) {
            return null
        }

        return stack.reversed().asSequence()
            .map { mapOpenToCloseBracket.getValue(it) }
            .joinToString("")
    }

    val mapBracketToPoints = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun calculateAutocompleteScore(line: String): Long {
        var score: Long = 0
        for (char in line) {
            score *= 5
            score += mapBracketToPoints.getValue(char)
        }
        return score
    }

    fun part2(input: List<String>): Long {
        val sortedScores = input
            .asSequence()
            .map { line -> completeLine(line) }
            .filterNotNull()
            .map { calculateAutocompleteScore(it) }
            .sorted()
            .toList()

        return sortedScores[sortedScores.size / 2]
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == (288957).toLong())

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
