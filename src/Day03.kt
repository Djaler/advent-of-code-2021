fun main() {
    fun part1(input: List<String>): Int {
        val size = input.size

        val oneCounts = IntArray(input.first().length)

        for (line in input) {
            for ((index, char) in line.withIndex()) {
                if (char == '1') {
                    oneCounts[index] += 1;
                }
            }
        }

        val gammaRate = oneCounts.joinToString("") { count -> if (count > size / 2) "1" else "0" }.toInt(2)
        val epsilonRate = oneCounts.joinToString("") { count -> if (count > size / 2) "0" else "1" }.toInt(2)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        fun filterByBitCriteria(
            input: List<String>,
            criteria: (withOne: List<String>, withZero: List<String>) -> Boolean
        ): String {
            var index = 0
            var currentInput = input

            while (currentInput.size > 1) {
                val grouped = currentInput.groupBy { it[index] }
                val withOne = grouped['1'] ?: emptyList()
                val withZero = grouped['0'] ?: emptyList()
                currentInput = if (criteria(withOne, withZero)) withOne else withZero
                index += 1
            }

            return currentInput.first()
        }

        val oxygenRating = filterByBitCriteria(input) { withOne, withZero -> withOne.size >= withZero.size }
            .toInt(2)
        val co2Rating = filterByBitCriteria(input) { withOne, withZero -> withOne.size < withZero.size }
            .toInt(2)

        return oxygenRating * co2Rating;
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
