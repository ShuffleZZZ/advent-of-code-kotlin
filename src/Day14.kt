private data class CurrentUnit(var pile: Int = 500, var height: Int = 0) {
    fun diagonalMove(piles: Map<Int, Set<Int>>): Boolean {
        if (piles[pile - 1] == null || !piles[pile - 1]!!.contains(height + 1)) {
            pile--
            height++

            return true
        }

        if (piles[pile + 1] == null || !piles[pile + 1]!!.contains(height + 1)) {
            pile++
            height++

            return true
        }

        return false
    }

    fun reset(): Boolean {
        if (pile == 500 && height == 0) return false

        pile = 500
        height = 0

        return true
    }
}

private fun getPoints(input: List<String>) = input.map { line ->
    line.split(" -> ")
        .map { it.split(',').map(String::toInt) }
        .map { (first, second) -> first to second }
}

private fun getPiles(points: List<List<Pair<Int, Int>>>): MutableMap<Int, MutableSet<Int>> {
    val piles = mutableMapOf<Int, MutableSet<Int>>()

    points.forEach { line ->
        line.zipWithNext().forEach { (start, end) ->
            val (minPoint, maxPoint) = if (start < end) start to end else end to start
            val (x, y) = maxPoint - minPoint

            for (i in 0..x) {
                for (j in 0..y) {
                    piles.computeIfAbsent(minPoint.first + i) { mutableSetOf() }.add(minPoint.second + j)
                }
            }
        }
    }

    return piles
}

private fun Set<Int>.fallUnitOrNull(height: Int) = this.filter { it > height }.minOrNull()

fun main() {
    fun part1(input: List<String>): Int {
        val piles = getPiles(getPoints(input))

        var cnt = 0
        val point = CurrentUnit()
        while (true) {
            val pile = piles[point.pile]
            point.height = pile?.fallUnitOrNull(point.height) ?: return cnt
            point.height--

            if (point.diagonalMove(piles)) continue

            pile.add(point.height)
            cnt++

            point.reset()
        }
    }

    fun part2(input: List<String>): Int {
        val piles = getPiles(getPoints(input))
        val floor = piles.values.maxOf { it.max() } + 2

        var cnt = 0
        val point = CurrentUnit()
        while (true) {
            val pile = piles.computeIfAbsent(point.pile) { mutableSetOf(floor) }

            point.height = pile.fallUnitOrNull(point.height) ?: floor
            point.height--

            if (point.height != floor - 1 && point.diagonalMove(piles)) continue

            pile.add(point.height)
            cnt++

            if (!point.reset()) return cnt
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
