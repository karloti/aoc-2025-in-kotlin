import kotlin.time.measureTimedValue

fun String.toSequence(delimiter: Char) = split(delimiter).let { it[0].toLong()..it[1].toLong() }.asSequence()

fun main() {
    fun part1(input: Sequence<String>): Long = input.sumOf { rangeString ->
        rangeString.toSequence('-').filter { isDoublePattern(it) }.sum()
    }

    fun part2(input: Sequence<String>): Long = input.sumOf { rangeString ->
        rangeString.toSequence('-').filter { isMultiPattern(it) }.sum()
    }

    val testInput = readInput("Day02_test", ',')
    val testInputStr = readInputAsText("Day02_test")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)
    check(part2Fast(testInputStr) == 4174379265L)

    val input = readInput("Day02", ',')
    val inputStr = readInputAsText("Day02")
    measureTimedValue { part1(input) }.println()
    measureTimedValue { part2(input) }.println()
    measureTimedValue { part2Fast(inputStr) }.println()
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

val POW10_DIVISORS = arrayOf(
    1L to intArrayOf(),
    10L to intArrayOf(),
    100L to intArrayOf(2),
    1_000L to intArrayOf(3),
    10_000L to intArrayOf(2, 4),
    100_000L to intArrayOf(5),
    1_000_000L to intArrayOf(2, 3, 6),
    10_000_000L to intArrayOf(7),
    100_000_000L to intArrayOf(2, 4, 8),
    1_000_000_000L to intArrayOf(3, 9),
    10_000_000_000L to intArrayOf(2, 5, 10),
    100_000_000_000L to intArrayOf(11),
    1_000_000_000_000L to intArrayOf(2, 3, 4, 6, 12),
    10_000_000_000_000L to intArrayOf(13),
    100_000_000_000_000L to intArrayOf(2, 7, 14),
    1_000_000_000_000_000L to intArrayOf(3, 5, 15),
    10_000_000_000_000_000L to intArrayOf(2, 4, 8, 16),
    100_000_000_000_000_000L to intArrayOf(17),
    1_000_000_000_000_000_000L to intArrayOf(2, 3, 6, 9, 18),
)

fun part2Fast(input: String): Long {
    val nums = Regex("\\d+").findAll(input).map { it.value.toLong() }.toList()
    var total = 0L
    var i = 0
    while (i < nums.size) {
        val lo = nums[i]
        val hi = nums[i + 1]
        var n = lo
        while (n <= hi) {
            val next = nextPeriodicFast(n)
            if (next <= hi) total += next
            n = next + 1
        }
        i += 2
    }
    return total
}

fun digitCount(x: Long): Int {
    var n = x
    var c = 0
    while (n > 0) {
        n /= 10; c++
    }
    return if (c == 0) 1 else c
}

fun repeatBlock(block: Long, k: Int, powBlock: Long): Long {
    var acc = block
    var i = 1
    while (i < k) {
        acc = acc * powBlock + block; i++
    }
    return acc
}

fun nextPeriodicFast(n: Long): Long {
    val d = digitCount(n)
    if (d == 1) return 11L

    var best = Long.MAX_VALUE
    val ks = POW10_DIVISORS[d].second

    for (idx in ks.indices) {
        val k = ks[idx]
        val m = d / k
        val powBlock = POW10_DIVISORS[m].first
        val topDiv = POW10_DIVISORS[m * (k - 1)].first

        val top = n / topDiv
        var cand = repeatBlock(top, k, powBlock)
        if (cand < n) cand = repeatBlock(top + 1, k, powBlock)
        if (cand < best) best = cand
        if (cand == n) return n
    }
    return best
}