import kotlin.math.absoluteValue

private const val TEST_LINE = 10
private const val LINE = 2_000_000

private const val TEST_SIZE = 20
private const val SIZE = 4_000_000

private val template = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

private fun getCoordinates(input: List<String>) = input.map { template.matchEntire(it)!!.destructured }
    .map { (x, y, bx, by) -> (x.toInt() to y.toInt()) to (bx.toInt() to by.toInt()) }

private fun MutableSet<Pair<Int, Int>>.addRange(other: Pair<Int, Int>) {
    for (pair in this) {
        if (pair.include(other)) {
            if (other.diff() > pair.diff()) {
                remove(pair)
                addRange(other)
            }

            return
        }
    }

    for (pair in this) {
        if (pair.intersect(other)) {
            remove(pair)
            addRange(
                pair.first.coerceAtMost(other.first)
                    to pair.second.coerceAtLeast(other.second)
            )
            return
        }
    }

    add(other)
}

private fun Set<Pair<Int, Int>>.amount() = sumOf { it.diff() + 1 }

private fun fillLine(
    coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    line: Int,
    leastX: Int = Int.MIN_VALUE,
    mostX: Int = Int.MAX_VALUE,
): Set<Pair<Int, Int>> {
    val xs = mutableSetOf<Pair<Int, Int>>()
    for ((sensor, beacon) in coordinates) {
        val beaconDistance = sensor.manhattan(beacon)
        val lineDistance = (line - sensor.second).absoluteValue
        val rangeWidth = beaconDistance - lineDistance

        if (rangeWidth < 0) continue

        xs.addRange(
            (sensor.first - rangeWidth).coerceAtLeast(leastX)
                to (sensor.first + rangeWidth).coerceAtMost(mostX)
        )
    }

    return xs
}

private fun frequency(x: Int, y: Int) = 1L * SIZE * x + y

fun main() {
    fun part1(input: List<String>, line: Int) = fillLine(getCoordinates(input), line).amount() - 1

    fun part2(input: List<String>, size: Int): Long? {
        val coordinates = getCoordinates(input)
        for (line in 0..size) {
            val xs = fillLine(coordinates, line, 0, size)

            if (xs.amount() != size + 1) {
                if (xs.size == 2) {
                    assert(xs.first().second + 1 == xs.last().first - 1)

                    return frequency(xs.first().second + 1, line)
                }
                assert(xs.size == 1)

                return frequency(if (xs.first().first == 0) size else 0, line)
            }
        }

        return null
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, TEST_LINE) == 26)
    check(part2(testInput, TEST_SIZE) == 56_000_011L)

    val input = readInput("Day15")
    println(part1(input, LINE))
    println(part2(input, SIZE))
}
