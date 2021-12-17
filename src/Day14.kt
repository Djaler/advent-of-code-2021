fun main() {
    fun part1(input: List<String>): Int {
        val template = input.first()

        val insertionRules = input.asSequence()
            .drop(2)
            .map { line -> line.split(" -> ").let { (pair, insertion) -> pair to insertion } }
            .toMap()

        fun step(template: String, rules: Map<String, String>): String {
            return template.zipWithNext { a, b ->
                val pair = "$a$b"
                val insertion = rules[pair]
                if (insertion == null) a else "$a$insertion"
            }.joinToString("", postfix = template.last().toString())
        }

        var currentTemplate = template

        repeat(10) {
            currentTemplate = step(currentTemplate, insertionRules)
        }

        val counts = currentTemplate.groupingBy { it }.eachCount()
        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        fun step(pairs: Map<String, Long>, rules: Map<String, String>): Map<String, Long> {
            val newPairs = mutableMapOf<String, Long>()

            pairs.forEach { (pair, count) ->
                val insertion = rules[pair]
                if (insertion != null) {
                    val firstNewPair = "${pair.first()}$insertion"
                    val secondNewPair = "$insertion${pair.last()}"
                    newPairs[firstNewPair] = (newPairs[firstNewPair] ?: 0) + count
                    newPairs[secondNewPair] = (newPairs[secondNewPair] ?: 0) + count
                } else {
                    newPairs[pair] = (newPairs[pair] ?: 0) + count
                }
            }

            return newPairs
        }

        fun calculateLettersCount(
            pairs: Map<String, Long>,
            template: String
        ): Map<Char, Long> {
            var letterCounts = mutableMapOf<Char, Long>()
            pairs.forEach { (pair, count) ->
                val first = pair.first()
                val last = pair.last()
                letterCounts[first] = (letterCounts[first] ?: 0) + count
                letterCounts[last] = (letterCounts[last] ?: 0) + count
            }

            letterCounts = letterCounts.mapValues { it.value / 2 }.toMutableMap()
            val first = template.first()
            letterCounts[first] = (letterCounts[first] ?: 0) + 1

            val last = template.last()
            if (first != last) {
                letterCounts[last] = (letterCounts[last] ?: 0) + 1
            }
            return letterCounts
        }

        val template = input.first()

        val insertionRules = input.asSequence()
            .drop(2)
            .map { line -> line.split(" -> ").let { (pair, insertion) -> pair to insertion } }
            .toMap()

        var pairs = template
            .zipWithNext { a, b -> "$a$b" }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        repeat(40) {
            pairs = step(pairs, insertionRules)
        }

        val letterCounts = calculateLettersCount(pairs, template)
        return letterCounts.maxOf { it.value } - letterCounts.minOf { it.value }
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
