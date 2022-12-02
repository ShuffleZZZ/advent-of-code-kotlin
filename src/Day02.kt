private const val RPS_SIZE = 3

private fun String.toInt() = when (this) {
    "A", "X" -> 1
    "B", "Y" -> 2
    "C", "Z" -> 3
    else -> error("Incorrect input")
}

private fun Pair<Int, Int>.game1() = second + when {
    first % RPS_SIZE + 1 == second -> 6
    first == second -> 3
    (first + 1) % RPS_SIZE + 1 == second -> 0
    else -> error("Incorrect input game 1")
}

private fun Pair<Int, Int>.game2() = when (second) {
    1 -> (first + 1) % RPS_SIZE + 1
    2 -> 3 + first
    3 -> 6 + first % RPS_SIZE + 1
    else -> error("Incorrect input game 2")
}

private fun parseInput(input: List<String>) =
    input.map { it.trim().split(' ', limit = 2) }.map { it.first().toInt() to it.last().toInt() }

fun main() {
    fun part1(input: List<String>) = parseInput(input).sumOf { it.game1() }

    fun part2(input: List<String>) = parseInput(input).sumOf { it.game2() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
