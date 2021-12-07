import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    fun part1(input: List<String>): Int {
        fun findMedian(numbers: Collection<Int>): Int {
            val sorted = numbers.sorted()
            if (numbers.size % 2 == 0) {
                return (sorted[numbers.size / 2] + sorted[numbers.size / 2 - 1]) / 2;
            }
            return sorted[numbers.size / 2];
        }

        fun calculateCost(from: Int, to: Int): Int {
            return abs(from - to)
        }

        val data = input.first().splitToSequence(',').map { it.toInt() }.toList()
        val median = findMedian(data)

        return data.sumOf { calculateCost(it, median) }
    }

    fun part2(input: List<String>): Int {
        fun getTriangleNumber(number: Int) = number * (number + 1) / 2

        fun calculateCost(from: Int, to: Int): Int {
            if (from == to) {
                return 0
            }
            val distance = abs(from - to)

            return getTriangleNumber(distance)
        }

        val data = input.first().splitToSequence(',').map { it.toInt() }.toList()
        val average = data.average()

        return listOf(floor(average).toInt(), ceil(average).toInt())
            .distinct()
            .minOf { target -> data.sumOf { calculateCost(it, target) } }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
