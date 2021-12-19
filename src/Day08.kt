@JvmInline
value class Signal(val value: Char)

@JvmInline
value class Pattern(val value: Set<Signal>)

@JvmInline
value class Digit(val value: Char)

data class Entry(val uniquePatterns: List<Pattern>, val output: List<Pattern>)

fun Set<Signal>.asPattern() = Pattern(this)

fun parsePattern(string: String): Pattern {
    return string.map { Signal(it) }.toSet().asPattern()
}

fun parseEntry(it: String): Entry {
    val (uniquePatterns, output) = it.split(" | ")

    return Entry(
        uniquePatterns.split(' ').map { parsePattern(it) },
        output.split(' ').map { parsePattern(it) }
    )
}

fun parseEntries(input: List<String>) = input.asSequence().map { parseEntry(it) }

fun main() {
    fun part1(input: List<String>): Int {
        val uniquePatternsLength = setOf(2, 4, 3, 7)

        return parseEntries(input)
            .flatMap { it.output }
            .count { pattern -> pattern.value.size in uniquePatternsLength }
    }

    fun part2(input: List<String>): Int {
        fun decodePatternMapping(uniquePatterns: List<Pattern>): Map<Pattern, Digit> {
            val patternsByLength = uniquePatterns.groupBy { it.value.size }
            val onePattern = patternsByLength[2]!!.first()
            val fourPattern = patternsByLength[4]!!.first()
            val sevenPattern = patternsByLength[3]!!.first()
            val eightPattern = patternsByLength[7]!!.first()

            val a = (sevenPattern.value - onePattern.value).first()

            val zeroSixAndNinePatterns = patternsByLength[6]!!
            val sixPatternIndex = zeroSixAndNinePatterns.indexOfFirst { !it.value.containsAll(onePattern.value) }
            val sixPattern = zeroSixAndNinePatterns[sixPatternIndex]
            val ninePatternIndex = zeroSixAndNinePatterns.indexOfFirst { it.value.containsAll(fourPattern.value) }
            val ninePattern = zeroSixAndNinePatterns[ninePatternIndex]
            val zeroPatternIndex = (0..2).first { it != sixPatternIndex && it != ninePatternIndex }
            val zeroPattern = zeroSixAndNinePatterns[zeroPatternIndex]

            val e = (eightPattern.value - ninePattern.value).first()
            val d = (eightPattern.value - zeroPattern.value).first()
            val c = (eightPattern.value - sixPattern.value).first()
            val f = (onePattern.value - c).first()

            val twoThreeAndFivePatterns = patternsByLength[5]!!
            val threePatternIndex = twoThreeAndFivePatterns.indexOfFirst { it.value.containsAll(setOf(a, c, d, f)) }
            val threePattern = twoThreeAndFivePatterns[threePatternIndex]
            val twoPatternIndex = twoThreeAndFivePatterns.indexOfFirst { !ninePattern.value.containsAll(it.value) }
            val twoPattern = twoThreeAndFivePatterns[twoPatternIndex]
            val fivePatternIndex = (0..2).first { it != twoPatternIndex && it != threePatternIndex }
            val fivePattern = twoThreeAndFivePatterns[fivePatternIndex]

            val b = (ninePattern.value - threePattern.value).first()
            val g = ((threePattern.value - sevenPattern.value) - d).first()

            return mapOf(
                setOf(a, b, c, e, f, g).asPattern() to Digit('0'),
                setOf(c, f).asPattern() to Digit('1'),
                setOf(a, c, d, e, g).asPattern() to Digit('2'),
                setOf(a, c, d, f, g).asPattern() to Digit('3'),
                setOf(b, c, d, f).asPattern() to Digit('4'),
                setOf(a, b, d, f, g).asPattern() to Digit('5'),
                setOf(a, b, d, e, f, g).asPattern() to Digit('6'),
                setOf(a, c, f).asPattern() to Digit('7'),
                setOf(a, b, c, d, e, f, g).asPattern() to Digit('8'),
                setOf(a, b, c, d, f, g).asPattern() to Digit('9'),
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
