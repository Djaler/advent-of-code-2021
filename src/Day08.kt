typealias Signal = Char
typealias Pattern = Set<Signal>
typealias Digit = Char

data class Entry(val uniquePatterns: List<Pattern>, val output: List<Pattern>)

fun parseEntry(it: String): Entry {
    val (uniquePatterns, output) = it.split(" | ")

    return Entry(
        uniquePatterns.split(' ').map { it.toSet() },
        output.split(' ').map { it.toSet() }
    )
}

fun parseEntries(input: List<String>) = input.asSequence().map { parseEntry(it) }

fun main() {
    fun part1(input: List<String>): Int {
        val uniquePatternsLength = setOf(2, 4, 3, 7)

        return parseEntries(input)
            .flatMap { it.output }
            .count { pattern -> pattern.size in uniquePatternsLength }
    }

    fun part2(input: List<String>): Int {
        fun decodePatternMapping(uniquePatterns: List<Pattern>): Map<Pattern, Digit> {
            val patternsByLength = uniquePatterns.groupBy { it.size }
            val onePattern = patternsByLength[2]!!.first()
            val fourPattern = patternsByLength[4]!!.first()
            val sevenPattern = patternsByLength[3]!!.first()
            val eightPattern = patternsByLength[7]!!.first()

            val a = (sevenPattern - onePattern).first()

            val zeroSixAndNinePatterns = patternsByLength[6]!!
            val sixPatternIndex = zeroSixAndNinePatterns.indexOfFirst { !it.containsAll(onePattern) }
            val sixPattern = zeroSixAndNinePatterns[sixPatternIndex]
            val ninePatternIndex = zeroSixAndNinePatterns.indexOfFirst { it.containsAll(fourPattern) }
            val ninePattern = zeroSixAndNinePatterns[ninePatternIndex]
            val zeroPatternIndex = (0..2).first { it != sixPatternIndex && it != ninePatternIndex }
            val zeroPattern = zeroSixAndNinePatterns[zeroPatternIndex]

            val e = (eightPattern - ninePattern).first()
            val d = (eightPattern - zeroPattern).first()
            val c = (eightPattern - sixPattern).first()
            val f = (onePattern - c).first()

            val twoThreeAndFivePatterns = patternsByLength[5]!!
            val threePatternIndex = twoThreeAndFivePatterns.indexOfFirst { it.containsAll(setOf(a, c, d, f)) }
            val threePattern = twoThreeAndFivePatterns[threePatternIndex]
            val twoPatternIndex = twoThreeAndFivePatterns.indexOfFirst { !ninePattern.containsAll(it) }
            val twoPattern = twoThreeAndFivePatterns[twoPatternIndex]
            val fivePatternIndex = (0..2).first { it != twoPatternIndex && it != threePatternIndex }
            val fivePattern = twoThreeAndFivePatterns[fivePatternIndex]

            val b = (ninePattern - threePattern).first()
            val g = ((threePattern - sevenPattern) - d).first()

            return mapOf(
                setOf(a, b, c, e, f, g) to '0',
                setOf(c, f) to '1',
                setOf(a, c, d, e, g) to '2',
                setOf(a, c, d, f, g) to '3',
                setOf(b, c, d, f) to '4',
                setOf(a, b, d, f, g) to '5',
                setOf(a, b, d, e, f, g) to '6',
                setOf(a, c, f) to '7',
                setOf(a, b, c, d, e, f, g) to '8',
                setOf(a, b, c, d, f, g) to '9',
            )
        }

        fun mapPatternsToDigits(patterns: List<Pattern>, mapping: Map<Pattern, Digit>): List<Digit> {
            return patterns.map { mapping.getValue(it) }
        }

        return parseEntries(input)
            .map { mapPatternsToDigits(it.output, decodePatternMapping(it.uniquePatterns)) }
            .sumOf { it.joinToString("").toInt() }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
