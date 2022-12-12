var start = 0 to 0
var end = 0 to 0

private fun heights(input: List<String>) =
    input.mapIndexed { i, line ->
        line.mapIndexed { j, char ->
            when (char) {
                'S' -> 'a'.also { start = i to j }
                'E' -> 'z'.also { end = i to j }
                else -> char
            } - 'a'
        }
    }

private fun dijkstra(heights: List<List<Int>>, start: Pair<Int, Int>): List<List<Int>> {
    val distances = List(heights.size) { MutableList(heights.first().size) { Int.MAX_VALUE } }
    val unvisited = distances.indexPairs().toMutableSet()

    distances[start] = 0
    var cur = start

    while (true) {
        unvisited.remove(cur)

        neighbours(cur).filter { (i, j) -> i in distances.indices && j in distances.first().indices }
            .filter { unvisited.contains(it) }.forEach {
                if (heights[it] <= heights[cur] + 1) {
                    distances[it] = distances[it].coerceAtMost(distances[cur] + 1)
                }
            }

        val next = unvisited.minByOrNull { distances[it] }
        if (next == null || distances[next] == Int.MAX_VALUE) break

        cur = next
    }

    return distances
}

fun main() {
    fun part1(input: List<String>) = dijkstra(heights(input), start)[end]

    fun part2(input: List<String>): Int {
        val heights = heights(input)

        return heights.indexPairs { it == 0 }.minOf { dijkstra(heights, it)[end] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
