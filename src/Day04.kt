fun rangesPairList(input: List<String>) = input.map { "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex().find(it)!!.destructured }
    .map { (it.component1().toInt() to it.component2().toInt()) to (it.component3().toInt() to it.component4().toInt()) }

fun main() {
    fun part1(input: List<String>) = rangesPairList(input).sumOf { it.first.include(it.second).toInt() }

    fun part2(input: List<String>) = rangesPairList(input).sumOf { it.first.intersect(it.second).toInt() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
