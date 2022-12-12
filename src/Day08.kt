private fun getGrid(input: List<String>) = input.map { it.map(Char::digitToInt).toTypedArray() }.toTypedArray()

private fun observeLines(grid: Array<Array<Int>>, res: Array<BooleanArray>, lineInds: IntRange, rowInds: IntProgression) {
    val maxRowInd = if (rowInds.isRange()) 0 else grid.first().size - 1

    for (i in lineInds) {
        var max = grid[i + 1][maxRowInd]
        for (j in rowInds) {
            if (grid[i + 1][j + 1] > max) {
                res[i][j] = true
                max = grid[i + 1][j + 1]
            }
        }
    }

    if (rowInds.isRange()) observeLines(grid, res, lineInds, rowInds.reversed())
}

private fun observeRows(grid: Array<Array<Int>>, res: Array<BooleanArray>, lineInds: IntProgression, rowInds: IntRange) {
    val maxLineInd = if (lineInds.isRange()) 0 else grid.size - 1

    for (j in rowInds) {
        var max = grid[maxLineInd][j + 1]
        for (i in lineInds) {
            if (grid[i + 1][j + 1] > max) {
                res[i][j] = true
                max = grid[i + 1][j + 1]
            }
        }
    }

    if (lineInds.isRange()) observeRows(grid, res, lineInds.reversed(), rowInds)
}

private fun scenicScore(grid: Array<Array<Int>>, spot: Pair<Int, Int>) =
    neighbours().map { observeTree(grid, spot, it) }.reduce(Int::times)


private fun observeTree(grid: Array<Array<Int>>, spot: Pair<Int, Int>, shift: Pair<Int, Int>): Int {
    var dist = 0
    var indexes = spot + shift
    while (indexes.first in grid.indices && indexes.second in grid.first().indices) {
        dist++

        if (grid[indexes.first][indexes.second] >= grid[spot.first][spot.second]) break

        indexes += shift
    }

    return dist
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = getGrid(input)
        val n = grid.size
        val m = grid.first().size
        val res = Array(n - 2) { BooleanArray(m - 2) { false } }

        observeLines(grid, res, res.indices, res.first().indices)
        observeRows(grid, res, res.indices, res.first().indices)

        return res.sumOf { it.count { e -> e } } + 2 * (n + m - 2)
    }

    fun part2(input: List<String>): Int {
        val grid = getGrid(input)
        val res = Array(grid.size - 2) { IntArray(grid.first().size - 2) { 0 } }

        for (i in res.indices) {
            for (j in res.indices) {
                res[i][j] = scenicScore(grid, i + 1 to j + 1)
            }
        }

        return res.maxOf { it.max() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
