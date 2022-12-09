import kotlin.math.absoluteValue
import kotlin.math.sign

private data class Cell(var x: Int = 0, var y: Int = 0) {

    fun move(other: Cell) {
        x += other.x
        y += other.y
    }

    fun isAttached(other: Cell) = (x - other.x).absoluteValue < 2 && (y - other.y).absoluteValue < 2

    fun follow(other: Cell) {
        x += (other.x - x).sign
        y += (other.y - y).sign
    }
}

private fun moveTheRope(moves: List<String>, rope: List<Cell>): Int {
    val res = HashSet<Cell>()
    res.add(Cell())

    for (move in moves) {
        val (direction, value) = move.split(" ")
        val step = when (direction) {
            "R" -> Cell(1)
            "L" -> Cell(-1)
            "U" -> Cell(y = 1)
            "D" -> Cell(y = -1)
            else -> error("incorrect input")
        }

        for (i in 0 until value.toInt()) {
            rope.first().move(step)

            for (j in 1 until rope.size) {
                if (rope[j].isAttached(rope[j - 1])) continue

                rope[j].follow(rope[j - 1])
            }

            res.add(rope.last().copy())
        }
    }

    return res.size
}

fun main() {
    fun part1(input: List<String>) = moveTheRope(input, List(2) { Cell() })

    fun part2(input: List<String>) = moveTheRope(input, List(10) { Cell() })

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
