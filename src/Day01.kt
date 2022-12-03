private fun getSortedCalories(input: List<List<String>>) = input.map { it.sumOf(String::toInt) }.sortedDescending()

fun main() {
    fun part1(input: List<List<String>>) = getSortedCalories(input).first()

    fun part2(input: List<List<String>>) = getSortedCalories(input).take(3).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readBlocks("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readBlocks("Day01")
    println(part1(input))
    println(part2(input))
}
