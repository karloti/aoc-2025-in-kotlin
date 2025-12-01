private val String.sign: Int get() = (if (first() == 'L') -1 else 1)
private val String.num: Int get() = drop(1).toInt()
private fun Int.pos(num: Int): Int = ((this + num) % 100 + 100) % 100

fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(50 to 0) { (total, zeroHits), line ->
            val pos = total.pos(line.sign * line.num)
            pos to if (pos == 0) zeroHits + 1 else zeroHits
        }.second
    }

    fun part2(input: List<String>): Int {
        return input.fold(50 to 0) { (total, zeroHits), line ->
            val list = List(line.num) { total.pos((it + 1) * line.sign) }
            list.last() to (zeroHits + list.count { it == 0 })
        }.second
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}