private val template = """
Monkey (\d+):
  Starting items: (.*?)
  Operation: new = old (.) (.+)
  Test: divisible by (\d+)
    If true: throw to monkey (\d+)
    If false: throw to monkey (\d+)
    """.trim().toRegex()

private data class MonkeyBusiness(
    val items: MutableList<ULong>,
    val op: (ULong, ULong) -> ULong,
    val value: String,
    val divider: ULong,
    val onTrue: Int,
    val onFalse: Int,
    var inspected: ULong = 0U,
) {
    fun throwDecision(worryLevel: ULong) = if (worryLevel % divider == 0.toULong()) onTrue else onFalse

    fun roundEnd() {
        inspected += items.size.toUInt()
        items.clear()
    }
}

private fun parseMonkeys(input: List<String>) = input.map { monkey ->
    val (_, list, sign, value, divider, onTrue, onFalse) = template.matchEntire(monkey)!!.destructured
    val items = list.split(", ").map(String::toULong).toMutableList()
    val operation: (ULong, ULong) -> ULong = when (sign.first()) {
        '+' -> ULong::plus
        '*' -> ULong::times
        else -> error("Incorrect input")
    }

    MonkeyBusiness(items, operation, value, divider.toULong(), onTrue.toInt(), onFalse.toInt())
}

private fun throwRounds(monkeys: List<MonkeyBusiness>, times: Int, calming: (ULong) -> ULong): ULong {
    val greatDivider = monkeys.map { it.divider }.reduce(ULong::times)

    repeat(times) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                val numberValue = monkey.value.toULongOrNull() ?: item
                val worryLevel = calming(monkey.op(item, numberValue)) % greatDivider

                monkeys[monkey.throwDecision(worryLevel)].items.add(worryLevel)
            }

            monkey.roundEnd()
        }
    }

    return monkeys.map { it.inspected }.sorted().takeLast(2).reduce(ULong::times)
}

fun main() {
    fun part1(input: List<String>) = throwRounds(parseMonkeys(input), 20) { it / 3U }

    fun part2(input: List<String>) = throwRounds(parseMonkeys(input), 10_000) { it }

    // test if implementation meets criteria from the description, like:
    val testInput = readRawBlocks("Day11_test")
    check(part1(testInput) == 10_605.toULong())
    check(part2(testInput) == 2_713_310_158.toULong())

    val input = readRawBlocks("Day11")
    println(part1(input))
    println(part2(input))
}
