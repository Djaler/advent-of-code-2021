fun main() {
    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        for (line in input) {
            val (direction, unitsString) = line.split(' ')
            val units = unitsString.toInt()
            when (direction) {
                "forward" -> horizontal += units
                "down" -> depth += units
                "up" -> depth -= units
            }
        }

        return horizontal * depth
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0
        for (line in input) {
            val (direction, unitsString) = line.split(' ')
            val units = unitsString.toInt()
            when (direction) {
                "forward" -> {
                    horizontal += units
                    depth += aim * units
                }
                "down" -> aim += units
                "up" -> aim -= units
            }
        }

        return horizontal * depth
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
