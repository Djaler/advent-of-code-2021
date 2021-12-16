fun main() {
    data class Coord(val x: Int, val y: Int)

    fun parseDots(input: List<String>): List<Coord> {
        return input
            .map { line ->
                line.split(',')
                    .map { it.toInt() }
                    .let { (x, y) -> Coord(x, y) }
            }
    }

    data class FoldInstruction(val axis: String, val position: Int)

    fun parseFoldInstructions(input: List<String>): List<FoldInstruction> {
        return input
            .map { line ->
                line.removePrefix("fold along ")
                    .split('=')
                    .let { (axis, position) -> FoldInstruction(axis, position.toInt()) }
            }
    }

    fun fold(dot: Coord, instruction: FoldInstruction): Coord {
        if (instruction.axis == "x") {
            if (dot.x < instruction.position) {
                return dot
            }
            return Coord(instruction.position - (dot.x - instruction.position), dot.y)
        } else {
            if (dot.y < instruction.position) {
                return dot
            }
            return Coord(dot.x, instruction.position - (dot.y - instruction.position))
        }
    }

    fun fold(dots: List<Coord>, instruction: FoldInstruction): List<Coord> {
        return dots.map { fold(it, instruction) }.distinct()
    }

    fun part1(input: List<String>): Int {
        val blankLineIndex = input.indexOfFirst { it.isEmpty() }
        val dots = parseDots(input.subList(0, blankLineIndex))
        val foldInstructions = parseFoldInstructions(input.subList(blankLineIndex + 1, input.size))

        val result = fold(dots, foldInstructions.first())

        return result.size
    }

    fun printDots(dots: List<Coord>) {
        var maxX = 0
        var maxY = 0
        dots.forEach { dot ->
            if (dot.x > maxX) {
                maxX = dot.x
            }
            if (dot.y > maxY) {
                maxY = dot.y
            }
        }

        val printData = List(maxY + 1) { MutableList(maxX + 1) { '.' }.toMutableList() }
        dots.forEach { dot -> printData[dot.y][dot.x] = '#' }

        printData.forEach { line ->
            line.forEach { print(it) }
            println()
        }
    }

    fun part2(input: List<String>) {
        val blankLineIndex = input.indexOfFirst { it.isEmpty() }
        var dots = parseDots(input.subList(0, blankLineIndex))
        val foldInstructions = parseFoldInstructions(input.subList(blankLineIndex + 1, input.size))

        foldInstructions.forEach { instruction ->
            dots = fold(dots, instruction)
        }

        printDots(dots)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}
