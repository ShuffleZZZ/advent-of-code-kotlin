private val DIVIDER_PACKETS = listOf("[[2]]", "[[6]]")

private sealed interface Signal : Comparable<Signal?> {
    override operator fun compareTo(signal: Signal?): Int
}

private data class SingleSignal(val element: Int) : Signal {
    fun toMultiSignal() = MultiSignal(listOf(this))

    override fun compareTo(signal: Signal?) = when (signal) {
        null -> 1
        is SingleSignal -> element.compareTo(signal.element)
        is MultiSignal -> toMultiSignal().compareTo(signal)
    }
}

private data class MultiSignal(val items: List<Signal>) : Signal {
    override fun compareTo(signal: Signal?): Int = when (signal) {
        null -> 1
        is SingleSignal -> compareTo(signal.toMultiSignal())
        is MultiSignal -> items.mapIndexed { i, e -> e.compareTo(signal.items.getOrNull(i)) }.firstOrNull { it != 0 }
            ?: items.size.compareTo(signal.items.size)
    }
}

private fun parseSignal(input: String): Signal {
    val res = mutableListOf<Signal>()

    var values = input.substring(1, input.length - 1)
    while (values.contains(',')) {
        val number = values.substringBefore(',').toIntOrNull()

        res += if (number != null) {
            SingleSignal(number)
        } else {
            val list = values.firstInnerList()
            parseSignal(list).also { values = values.substringAfter(list) }
        }

        values = values.substringAfter(',')
    }

    if (values.contains('[')) {
        res += parseSignal(values)
    } else if (values.isNotEmpty()) {
        res += SingleSignal(values.toInt())
    }

    return MultiSignal(res)
}

private fun String.firstInnerList(): String {
    var balance = 0
    for (i in indices) {
        when (get(i)) {
            '[' -> balance++
            ']' -> balance--
        }

        if (balance == 0) return substring(0, i + 1) // assuming given string starts with '['.
    }

    return this
}

fun main() {
    fun part1(input: List<List<String>>) =
        input.map { (first, second) -> parseSignal(first) to parseSignal(second) }.withIndex()
            .filter { (_, value) -> value.first < value.second }.sumOf { (i) -> i + 1 }


    fun part2(input: List<List<String>>): Int {
        val signals = input.flatten().toMutableList()
        signals.addAll(DIVIDER_PACKETS)

        val sortedSignals = signals.map(::parseSignal).sorted()

        return DIVIDER_PACKETS.map { sortedSignals.indexOf(parseSignal(it)) + 1 }.reduce(Int::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readBlocks("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readBlocks("Day13")
    println(part1(input))
    println(part2(input))
}
