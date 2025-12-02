fun String.toSequence(delimiter: Char) = split(delimiter).let { it[0].toLong()..it[1].toLong() }.asSequence()

fun main() {
    fun part1(input: Sequence<String>): Long = input.sumOf { rangeString ->
        rangeString.toSequence('-').filter { isDoublePattern(it) }.sum()
    }

    fun part2(input: Sequence<String>): Long = input.sumOf { rangeString ->
        rangeString.toSequence('-').filter { isMultiPattern(it) }.sum()
    }

    val testInput = readInput("Day02_test", ',')
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02", ',')
    part1(input).println()
    part2(input).println()
}

fun isDoublePattern(number: Long): Boolean {
    val s = number.toString()
    val len = s.length
    if (len % 2 != 0) return false
    val mid = len / 2
    return s.take(mid) == s.drop(mid)
}

fun isMultiPattern(number: Long): Boolean {
    if (number < 10) return false
    val s = number.toString()
    val n = s.length
    val lps = IntArray(n)
    var length = 0
    var i = 1
    while (i < n) {
        if (s[i] == s[length]) {
            length++
            lps[i] = length
            i++
        } else {
            if (length != 0) {
                length = lps[length - 1]
            } else {
                lps[i] = 0
                i++
            }
        }
    }
    val lastLps = lps[n - 1]
    val patternLen = n - lastLps
    return lastLps > 0 && n % patternLen == 0
}