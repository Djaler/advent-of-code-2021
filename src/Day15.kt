import java.util.*

typealias Grid = List<List<Int>>

fun main() {
    fun parseGrid(input: List<String>): Grid {
        return input
            .map { line -> line.map { char -> char.digitToInt() } }
    }

    data class Coord(val x: Int, val y: Int)

    fun findNeighbours(coord: Coord, grid: Grid): Collection<Coord> {
        return buildSet {
            if (coord.x > 0) {
                add(Coord(coord.x - 1, coord.y))
            }
            if (coord.x < grid[coord.y].size - 1) {
                add(Coord(coord.x + 1, coord.y))
            }
            if (coord.y > 0) {
                add(Coord(coord.x, coord.y - 1))
            }
            if (coord.y < grid.size - 1) {
                add(Coord(coord.x, coord.y + 1))
            }
        }
    }

    data class PrioritizedItem<T>(val item: T, val priority: Int)

    fun findCost(start: Coord, end: Coord, grid: Grid): Int {
        val frontier = PriorityQueue<PrioritizedItem<Coord>>(compareBy { it.priority })
        frontier.add(PrioritizedItem(start, 0))

        val costs = mutableMapOf<Coord, Int>()
        costs[start] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.remove()

            if (current.item == end) {
                break
            }

            for (next in findNeighbours(current.item, grid)) {
                val newCost = costs.getValue(current.item) + grid[next.y][next.x]

                val nextCost = costs[next]
                if (nextCost == null || newCost < nextCost) {
                    costs[next] = newCost
                    frontier.add(PrioritizedItem(next, newCost))
                }
            }
        }

        return costs.getValue(end)
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)

        val start = Coord(0, 0)
        val end = Coord(grid.size - 1, grid.size - 1)

        return findCost(start, end, grid)
    }

    fun mutableLists(grid: Grid): Grid {
        val levelToGrid = mutableMapOf<Int, Grid>()
        levelToGrid[0] = grid
        for (level in (1..8)) {
            val previousLevelGrid = levelToGrid.getValue(level - 1)
            val incrementedGrid = previousLevelGrid.map { line ->
                line.map { if (it < 9) it + 1 else 1 }
            }
            levelToGrid[level] = incrementedGrid
        }
        val mutableGrid = grid.asSequence().map { it.toMutableList() }.toMutableList()

        fun expandRowToRight(
            currentLevel: Int,
            currentRow: MutableList<Int>,
            levels: MutableMap<Int, Grid>,
            rowIndex: Int
        ) {
            for (levelRight in currentLevel + 1..currentLevel + 4) {
                currentRow.addAll(levels.getValue(levelRight)[rowIndex])
            }
        }

        for ((rowIndex, row) in mutableGrid.withIndex()) {
            expandRowToRight(0, row, levelToGrid, rowIndex)
        }

        for (levelDown in 1..4) {
            for ((rowIndex, row) in levelToGrid.getValue(levelDown).withIndex()) {
                val newRow = row.toMutableList()

                expandRowToRight(levelDown, newRow, levelToGrid, rowIndex)

                mutableGrid.add(newRow)
            }
        }
        return mutableGrid
    }

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)

        val mutableGrid = mutableLists(grid)

        val start = Coord(0, 0)
        val end = Coord(mutableGrid.size - 1, mutableGrid.size - 1)

        return findCost(start, end, mutableGrid)
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
