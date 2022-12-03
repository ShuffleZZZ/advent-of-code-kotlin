private fun compartments(input: List<String>) = input.map { it.trim().chunked(it.length / 2) }
private fun groups(input: List<String>) = input.windowed(3, 3)

private fun List<List<String>>.intersect() = flatMap { it.map(String::toSet).reduce(Set<Char>::intersect) }
private fun Char.toPriority() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

fun main() {
    fun part1(input: List<String>) = compartments(input).intersect().sumOf { it.toPriority() }

    fun part2(input: List<String>) = groups(input).intersect().sumOf { it.toPriority() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
