import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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
