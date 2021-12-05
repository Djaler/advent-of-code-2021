fun main() {
    data class Coordinate(val x: Int, val y: Int)
    data class Line(val start: Coordinate, val end: Coordinate) {
        val horizontal
            get() = start.y == end.y

        val vertical
            get() = start.x == end.x
    }

    fun parseCoordinate(input: String): Coordinate {
        val (x, y) = input.split(',')
        return Coordinate(x.toInt(), y.toInt())
    }

    fun parseLine(input: String): Line {
        val (start, end) = input.split(" -> ")
        return Line(parseCoordinate(start), parseCoordinate(end))
    }

    infix fun Int.to(to: Int) = if (to >= this) this..to else this downTo to

    fun part1(input: List<String>): Int {
        fun getCoveredDots(line: Line): List<Coordinate> {
            if (line.horizontal) {
                return (line.start.x to line.end.x).map { x -> Coordinate(x, line.start.y) }
            }
            if (line.vertical) {
                return (line.start.y to line.end.y).map { y -> Coordinate(line.start.x, y) }
            }
            return emptyList()
        }

        return input
            .asSequence()
            .map { parseLine(it) }
            .flatMap { getCoveredDots(it) }
            .groupBy { it }
            .count { it.value.size > 1 }
    }

    fun part2(input: List<String>): Int {
        fun getCoveredDots(line: Line): List<Coordinate> {
            if (line.horizontal) {
                return (line.start.x to line.end.x).map { x -> Coordinate(x, line.start.y) }
            }
            if (line.vertical) {
                return (line.start.y to line.end.y).map { y -> Coordinate(line.start.x, y) }
            }

            return (line.start.x to line.end.x).zip(line.start.y to line.end.y)
                .map { (x, y) -> Coordinate(x, y) }
        }

        return input
            .asSequence()
            .map { parseLine(it) }
            .flatMap { getCoveredDots(it) }
            .groupBy { it }
            .count { it.value.size > 1 }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
