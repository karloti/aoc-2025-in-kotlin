import kotlin.time.measureTimedValue

fun main() {
    fun part1(lines: List<String>) = lines.sumOf { findMaxVoltageForBank(it, 2) }
    fun part2(lines: List<String>) = lines.sumOf { findMaxVoltageForBank(it, 12) }

    val testInput = readInput("Day03_test")
    val p1TestResult = part1(testInput)
    val p2TestResult = part2(testInput)
    check(p1TestResult == 357L)
    check(p2TestResult == 3121910778619L)

    val input = readInput("Day03")
    println("--- Part 1 ---")
    measureTimedValue { part1(input) }.println()
    println("--- Part 2 ---")
    measureTimedValue { part2(input) }.println()
}

fun findMaxVoltageForBank(bank: String, target: Int): Long {
    val bankLength = bank.length
    val stack = mutableListOf<Char>()
    bank.forEachIndexed { i, c ->
        while (stack.isNotEmpty() && c > stack.last() && (stack.lastIndex + bankLength - i) >= target) stack.removeLast()
        if (stack.size < target) stack.add(c)
    }
    return stack.joinToString("").toLong()
}