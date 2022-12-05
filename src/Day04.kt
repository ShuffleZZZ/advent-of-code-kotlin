private fun rangesPairList(input: List<String>) = input.map { "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex().find(it)!!.destructured }
    .map { (l1, l2, r1, r2) -> (l1.toInt() to l2.toInt()) to (r1.toInt() to r2.toInt()) }

fun main() {
    fun part1(input: List<String>) = rangesPairList(input).sumOf { (first, second) -> first.include(second).toInt() }

    fun part2(input: List<String>) = rangesPairList(input).sumOf { (first, second) -> first.intersect(second).toInt() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
