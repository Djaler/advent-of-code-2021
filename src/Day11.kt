typealias Grid = List<MutableList<Int>>

fun main() {
    fun parseGrid(input: List<String>): Grid {
        return input.map { line -> line.map { it.digitToInt() }.toMutableList() }
    }

    data class Coord(val x: Int, val y: Int)

    operator fun Grid.get(coord: Coord) = this[coord.y][coord.x]
    operator fun Grid.set(coord: Coord, value: Int) {
        this[coord.y][coord.x] = value
    }

    fun <T, S> Iterable<T>.cartesianProduct(other: Iterable<S>): List<Pair<T, S>> {
        return flatMap { first -> other.map { second -> first to second } }
    }

    fun findAdjacent(coord: Coord, grid: Grid): Collection<Coord> {
        return (coord.x - 1..coord.x + 1).cartesianProduct(coord.y - 1..coord.y + 1)
            .asSequence()
            .filter { (x, y) -> x >= 0 }
            .filter { (x, y) -> y >= 0 }
            .filter { (x, y) -> x < grid.first().size }
            .filter { (x, y) -> y < grid.size }
            .filter { (x, y) -> !(x == coord.x && y == coord.y) }
            .map { (x, y) -> Coord(x, y) }
            .toList()
    }

    fun shouldFlash(coord: Coord, grid: Grid, flashing: Set<Coord>): Boolean {
        return grid[coord] > 9 && coord !in flashing
    }

    fun flash(coord: Coord, grid: Grid, flashing: MutableSet<Coord>) {
        flashing.add(coord)
        val findAdjacent = findAdjacent(coord, grid)
        for (adjacent in findAdjacent) {
            if (adjacent in flashing) {
                continue
            }
            grid[adjacent] += 1
            if (shouldFlash(adjacent, grid, flashing)) {
                flash(adjacent, grid, flashing)
            }
        }
    }

    fun step(grid: Grid): Int {
        for (row in grid) {
            for (x in row.indices) {
                row[x] += 1
            }
        }
        val flashing = mutableSetOf<Coord>()
        for ((y, row) in grid.withIndex()) {
            for ((x, value) in row.withIndex()) {
                val coord = Coord(x, y)
                if (shouldFlash(coord, grid, flashing)) {
                    flash(coord, grid, flashing)
                }
            }
        }
        for (coord in flashing) {
            grid[coord] = 0
        }

        return flashing.size
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)
        var flashesCount = 0

        repeat(100) {
            flashesCount += step(grid)
        }

        return flashesCount
    }

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)
        val totalCount = grid.size * grid.first().size

        var stepNumber = 0
        while (true) {
            stepNumber++
            val flashingCount = step(grid)
            if (flashingCount == totalCount) {
                return stepNumber
            }
        }
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
