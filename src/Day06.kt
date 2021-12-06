fun main() {
    fun part1(input: List<String>): Int {
        fun parseInitialState(input: List<String>): List<Int> {
            return input.first().splitToSequence(',').map { it.toInt() }.toList()
        }

        fun tick(state: List<Int>): List<Int> {
            return state.flatMap { timer -> if (timer == 0) listOf(6, 8) else listOf(timer - 1) }
        }

        var state = parseInitialState(input)

        repeat(80) {
            state = tick(state)
        }

        return state.size
    }

    fun part2(input: List<String>): Long {
        fun parseInitialState(input: List<String>): Map<Int, Long> {
            return input.first()
                .splitToSequence(',')
                .map { it.toInt() }
                .groupingBy { it }
                .eachCount()
                .mapValues { (_, value) -> value.toLong() }
        }

        fun tick(state: Map<Int, Long>): Map<Int, Long> {
            val newState = mutableMapOf<Int, Long>()

            for ((timer, count) in state) {
                val nextTimer = if (timer > 0) timer - 1 else 6
                newState[nextTimer] = (newState[nextTimer] ?: 0) + count

                if (timer == 0) {
                    newState[8] = count
                }
            }

            return newState
        }

        var state = parseInitialState(input)

        repeat(256) {
            state = tick(state)
        }

        return state.values.sum()
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
