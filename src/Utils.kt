import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.sign

fun Boolean.toInt() = if (this) 1 else 0

fun <T, K> Pair<T, K>.compareTo(other: Pair<T, K>) where T : Comparable<T>, K : Comparable<K> =
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
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
