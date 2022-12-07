private const val MAX_DIR_SIZE = 100_000
private const val DISK_SPACE = 70_000_000
private const val UPDATE_SIZE = 30_000_000

private data class FS(
    val name: String,
    val parent: FS?, // null for root dir only.
) {
    val files: MutableList<Int> = mutableListOf()
    val folders: MutableList<FS> = mutableListOf()
    var size: Int = 0
        get() {
            if (field == 0) field = files.sum() + folders.sumOf { it.size }
            return field
        }

    fun fill(instructions: List<String>): FS {
        var curDir = this

        for (instruction in instructions) {
            when {
                instruction.startsWith("cd ") -> curDir = curDir.handleCd(instruction)
                instruction.startsWith("ls ") -> curDir.handleLs(instruction)
                else -> error("Incorrect input")
            }
        }

        return this
    }

    private fun handleCd(instruction: String): FS {
        val dir = instruction.split(" ").last()
        if (dir == "/") return this

        return if (dir == "..") this.parent!! else this.cd(dir)
    }

    private fun cd(name: String) = this.folders.first { it.name == name } // presuming absolute paths unique.

    private fun handleLs(instruction: String): FS {
        val content = instruction.split(" ").drop(1).chunked(2).map { it.first() to it.last() }

        for ((first, second) in content) {
            when {
                first == "dir" -> this.folders.add(FS(second, this))
                first.toIntOrNull() != null -> this.files.add(first.toInt()) // presuming file names unique.
                else -> error("Incorrect input")
            }
        }

        return this
    }

    fun <T> map(f: (FS) -> T): List<T> {
        val res = mutableListOf(f(this))
        res.addAll(folders.flatMap { it.map(f) })
        return res
    }

    fun print(indent: Int = 0) {
        repeat(indent) { print("  - ") }
        println(this)
        folders.map { it.print(indent + 1) }
    }

    override fun toString() = "FS($name, $files, $size)"
}

fun getInstructions(input: List<String>) = input.joinToString(" ").split("$ ").drop(1).map(String::trim)

fun main() {
    fun part1(input: List<String>) =
        FS("/", null).fill(getInstructions(input)).map { it.size }.filter { it <= MAX_DIR_SIZE }.sum()

    fun part2(input: List<String>): Int {
        val root = FS("/", null)
        root.fill(getInstructions(input))

        val sizes = root.map { it.size }
        val required = UPDATE_SIZE - (DISK_SPACE - sizes.first())

        return sizes.filter { it >= required }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readBlocks("Day07_test").first()
    check(part1(testInput) == 95_437)
    check(part2(testInput) == 24_933_642)

    val input = readBlocks("Day07").first()
    println(part1(input))
    println(part2(input))
}
