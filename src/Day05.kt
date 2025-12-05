import kotlin.math.max
import kotlin.time.measureTimedValue

fun main() {
    val testInput = readInputAsText("Day05_test")
    println("--- RUNNING TESTS ---")
    val (testPart1, testPart2) = solveDay5(testInput)
    check(testPart1 == 3) { "Test Failed Part 1: Expected 3, got $testPart1" }
    check(testPart2 == 14L) { "Test Failed Part 2: Expected 14, got $testPart2" }
    println("Part 1 Test Result: $testPart1")
    println("Part 2 Test Result: $testPart2")
    println("---------------------")
//    val realInput = readInputAsText("Day05")
    val realInput = generateWorstCaseInput(1_000_000)
    val (result, duration) = (1..10).map { measureTimedValue { solveDay5(realInput) } }.minBy { it.duration }
    println("Final Answer Part 1: ${result.first}")
    println("Final Answer Part 2: ${result.second}")
    println("Time: $duration")
}

fun solveDay5(input: String): Pair<Int, Long> {
    val (ranges, ids) = input.trim().split(Regex("\\R\\R\\R\\R"))
        .let { (s, e) -> s.lineSequence() to e.lineSequence() }

    val sortedRanges = ranges
        .map { line -> line.split('-').let { (left, right) -> left.toLong()..right.toLong() } }
        .sortedBy { it.first }

    val mergedRanges = sortedRanges.fold(mutableListOf<LongRange>()) { acc, next ->
        val last = acc.lastOrNull()
        if (last != null && next.first <= last.last) {
            acc[acc.lastIndex] = last.first..max(last.last, next.last)
        } else {
            acc.add(next)
        }
        acc
    }

    val part1Count = ids.map(String::toLong).count { id ->
        mergedRanges.binarySearch { range ->
            when {
                id in range -> 0
                id < range.first -> 1
                else -> -1
            }
        } >= 0
    }

    val part2Count = mergedRanges.sumOf { it.last - it.first + 1L }

    return Pair(part1Count, part2Count)
}

fun generateWorstCaseInput(n: Int): String {
    val sb = StringBuilder()
    for (i in 0 until n) {
        val start = i * 10L
        val end = start + 5
        sb.append("$start-$end\n")
    }
    sb.append("\n\r\n\r")
    for (i in 0 until n) {
        sb.append("${i * 5}\n")
    }
    return sb.toString()
}
