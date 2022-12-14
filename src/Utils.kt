import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue
import kotlin.math.sign

fun Boolean.toInt() = if (this) 1 else 0

fun IntProgression.isRange() = step.sign > 0

operator fun <T> List<List<T>>.get(ind: Pair<Int, Int>) = this[ind.first][ind.second]

operator fun <T> List<MutableList<T>>.set(ind: Pair<Int, Int>, value: T) {
    this[ind.first][ind.second] = value
}

fun <T> List<List<T>>.indexPairs(predicate: (T) -> Boolean = { true }) =
    this.indices.flatMap { i ->
        this.first().indices.map { j -> i to j }
    }.filter { predicate(this[it]) }

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = first + other.first to second + other.second

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = first - other.first to second - other.second

operator fun <T, K> Pair<T, K>.compareTo(other: Pair<T, K>) where T : Comparable<T>, K : Comparable<K> =
    if (first == other.first) second.compareTo(other.second) else first.compareTo(other.first)

/**
 * Pairs intersect as ranges.
 */
fun <T> Pair<T, T>.intersect(other: Pair<T, T>) where T : Comparable<T> =
    if (this.compareTo(other) < 0) second >= other.first else first <= other.second

/**
 * One of pairs fully includes other as range.
 */
fun <T, K> Pair<T, K>.include(other: Pair<T, K>) where T : Comparable<T>, K : Comparable<K> =
    first.compareTo(other.first).sign + second.compareTo(other.second).sign in -1..1

/**
 * Returns list of pairs with indexes of non-diagonal neighbours' shifts in 2D array.
 */
fun neighbours() =
    (-1..1).flatMap { i ->
        (-1..1).filter { j -> (i + j).absoluteValue == 1 }.map { j -> i to j }
    }

/**
 * Returns list of pairs with indexes of given point non-diagonal neighbours in 2D array.
 */
fun neighbours(point: Pair<Int, Int>) = neighbours().map { it + point }

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Reads blocks of lines separated by empty line.
 */
fun readBlocks(name: String) = File("src", "$name.txt")
    .readText()
    .trim('\n')
    .split("\n\n")
    .map { it.split('\n') }

/**
 * Reads blocks separated by empty line.
 */
fun readRawBlocks(name: String) = File("src", "$name.txt")
    .readText()
    .trim('\n')
    .split("\n\n")

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
