import java.util.*

fun stackPeeks(input: List<List<String>>, move: (List<Stack<Char>>, Int, Int, Int) -> Unit): String {
    val (towers, instructions) = input
    val stacksSize = towers.last().split("\\s+".toRegex()).last().toInt()

    val stacks = List(stacksSize) { Stack<Char>() }
    for (i in (0 until towers.size - 1).reversed()) {
        for (j in 1 until towers[i].length step 4) {
            if (towers[i][j] != ' ') {
                stacks[j / 4].push(towers[i][j])
            }
        }
    }

    for (instruction in instructions) {
        val (amount, from, to) = "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(instruction)!!.destructured
        move(stacks, amount.toInt(), from.toInt(), to.toInt())
    }

    return stacks.map { it.peek() }.joinToString("")
}

fun main() {
    fun part1(input: List<List<String>>) =
        stackPeeks(input) { stacks: List<Stack<Char>>, amount: Int, from: Int, to: Int ->
            repeat(amount) { stacks[to - 1].push(stacks[from - 1].pop()) }
        }

    fun part2(input: List<List<String>>) =
        stackPeeks(input) { stacks: List<Stack<Char>>, amount: Int, from: Int, to: Int ->
            stacks[to - 1].addAll(stacks[from - 1].takeLast(amount))
            repeat(amount) { stacks[from - 1].pop() }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readBlocks("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readBlocks("Day05")
    println(part1(input))
    println(part2(input))
}
