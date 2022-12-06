const val UNIQUE_PREFIX_SIZE_1 = 4
const val UNIQUE_PREFIX_SIZE_2 = 14

private fun uniquePrefixEnd(input: String, size: Int) =
    input.windowed(size).indexOfFirst { it.toSet().size == it.length } + size

fun main() {
    fun part1(input: String) = uniquePrefixEnd(input, UNIQUE_PREFIX_SIZE_1)

    fun part2(input: String) = uniquePrefixEnd(input, UNIQUE_PREFIX_SIZE_2)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test").first()
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06").first()
    println(part1(input))
    println(part2(input))
}
