private const val SCREEN_WIDTH = 40
private const val SCREEN_SIZE = 6 * SCREEN_WIDTH
private const val SIGNAL_STRENGTH = SCREEN_WIDTH / 2

private fun cycleSignals(input: List<String>): List<Int> {
    val signals = MutableList(2 * input.size + 1) { 1 }

    var cycle = 0
    for (i in input.indices) {
        signals[cycle + 1] = signals[cycle]

        val instruction = input[i].split(" ")
        if (instruction.first() == "addx") {
            signals[++cycle + 1] = signals[cycle] + instruction.last().toInt()
        }

        cycle++
    }

    return signals
}

fun main() {
    fun part1(input: List<String>): Int {
        val signals = cycleSignals(input)

        return (SIGNAL_STRENGTH..SCREEN_SIZE - SIGNAL_STRENGTH step SCREEN_WIDTH).sumOf { it * signals[it - 1] }
    }

    fun part2(input: List<String>): String {
        val signals = cycleSignals(input).take(SCREEN_SIZE)

        return signals.chunked(SCREEN_WIDTH) { line ->
            line.mapIndexed { ind, signal -> if (signal in ind - 1..ind + 1) '#' else '.' }.joinToString("")
        }.joinToString("\n")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13_140)
    println(part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
