fun main() {
    data class Node(val id: String) {
        val small = id.lowercase() == id
    }

    fun parseGraph(input: List<String>): Map<Node, Set<Node>> {
        val nodesToEdges = mutableMapOf<Node, Set<Node>>()
        for (line in input) {
            val (from, to) = line.split('-').map { Node(it) }
            nodesToEdges.merge(from, setOf(to)) { current, new -> current + new }
            nodesToEdges.merge(to, setOf(from)) { current, new -> current + new }
        }

        return nodesToEdges
    }

    fun part1(input: List<String>): Int {
        fun findPathsToEnd(
            currentNode: Node,
            graph: Map<Node, Set<Node>>,
            visitedSmallNodes: Set<Node>,
            currentPath: List<Node>
        ): List<List<Node>> {
            val targetNodes = graph.getValue(currentNode)
            val paths = mutableListOf<List<Node>>()
            for (targetNode in targetNodes) {
                val newPath = currentPath + targetNode

                if (targetNode.id == "end") {
                    paths.add(newPath)
                    continue
                }

                if (targetNode.small && targetNode in visitedSmallNodes) {
                    continue
                }

                paths.addAll(
                    findPathsToEnd(
                        targetNode,
                        graph,
                        if (targetNode.small) visitedSmallNodes + targetNode else visitedSmallNodes,
                        newPath
                    )
                )
            }

            return paths
        }

        val graph = parseGraph(input)
        val start = Node("start")

        val paths = findPathsToEnd(
            start,
            graph,
            setOf(start),
            listOf(start)
        )

        return paths.size
    }

    fun part2(input: List<String>): Int {
        fun findPathsToEnd(
            currentNode: Node,
            graph: Map<Node, Set<Node>>,
            visitedSmallNodes: Set<Node>,
            smallNodeVisitedTwice: Boolean,
            currentPath: List<Node>
        ): List<List<Node>> {
            val targetNodes = graph.getValue(currentNode)
            val paths = mutableListOf<List<Node>>()
            for (targetNode in targetNodes) {
                val newPath = currentPath + targetNode

                if (targetNode.id == "end") {
                    paths.add(newPath)
                    continue
                }

                if (targetNode.id == "start") {
                    continue
                }

                if (targetNode.small && targetNode in visitedSmallNodes) {
                    if (!smallNodeVisitedTwice) {
                        paths.addAll(
                            findPathsToEnd(
                                targetNode,
                                graph,
                                visitedSmallNodes + targetNode,
                                true,
                                newPath
                            )
                        )
                    }
                    continue
                }
                paths.addAll(
                    findPathsToEnd(
                        targetNode,
                        graph,
                        if (targetNode.small) visitedSmallNodes + targetNode else visitedSmallNodes,
                        smallNodeVisitedTwice,
                        newPath
                    )
                )

            }

            return paths
        }

        val graph = parseGraph(input)
        val start = Node("start")

        val paths = findPathsToEnd(
            start,
            graph,
            setOf(start),
            false,
            listOf(start)
        )

        return paths.size
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
