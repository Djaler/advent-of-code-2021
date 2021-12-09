typealias HeightMap = List<List<Int>>

fun main() {
    fun parseHeightMap(input: List<String>): HeightMap = input.map { line -> line.map { it.digitToInt() } }

    data class Coord(val x: Int, val y: Int)

    fun HeightMap.get(coord: Coord) = this[coord.y][coord.x]

    fun findNeighbours(coord: Coord, heightMap: HeightMap): Collection<Coord> {
        return buildSet {
            if (coord.x > 0) {
                add(Coord(coord.x - 1, coord.y))
            }
            if (coord.x < heightMap[coord.y].size - 1) {
                add(Coord(coord.x + 1, coord.y))
            }
            if (coord.y > 0) {
                add(Coord(coord.x, coord.y - 1))
            }
            if (coord.y < heightMap.size - 1) {
                add(Coord(coord.x, coord.y + 1))
            }
        }
    }

    fun isLowPoint(coord: Coord, heightMap: HeightMap): Boolean {
        val value = heightMap.get(coord)
        return findNeighbours(coord, heightMap).all { heightMap.get(it) > value }
    }

    fun findLowPoints(heightMap: HeightMap) =
        heightMap.indices.asSequence()
            .flatMap { y -> heightMap.first().indices.asSequence().map { x -> Coord(x, y) } }
            .filter { coord -> isLowPoint(coord, heightMap) }

    fun part1(input: List<String>): Int {
        val heightMap = parseHeightMap(input)

        return findLowPoints(heightMap)
            .sumOf { heightMap.get(it) + 1 }
    }

    fun findBasins(coord: Coord, heightMap: HeightMap): Collection<Coord> {
        val visitedCoords = mutableSetOf(coord)

        fun visitNeighbours(coord: Coord) {
            findNeighbours(coord, heightMap)
                .asSequence()
                .filter { it !in visitedCoords }
                .filter { heightMap.get(it) < 9 }
                .forEach {
                    visitedCoords.add(it)
                    visitNeighbours(it)
                }
        }

        visitNeighbours(coord)

        return visitedCoords
    }

    fun part2(input: List<String>): Int {
        val heightMap = parseHeightMap(input)

        val basins = findLowPoints(heightMap)
            .map { findBasins(it, heightMap) }
            .toList()

        return basins.sortedByDescending { it.size }
            .take(3)
            .map { it.size }
            .reduce { acc, size -> acc * size }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
