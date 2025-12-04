import java.util.ArrayDeque
import kotlin.time.measureTimedValue

fun main() {
    fun partOneAndTwo(lines: List<String>): Pair<Int, Int> {
        data class Cell(var counts: Int, var isPaper: Boolean, var removed: Boolean)
        data class Offset(val row: Int, val col: Int)
        val (rows, cols) = lines.size to lines.first().length
        val totalCells = rows * cols
        val cells = List(totalCells) { Cell(counts = 0, isPaper = false, removed = false) }
        val n4 = listOf(Offset(0, 1), Offset(1, -1), Offset(1, 0), Offset(1, 1))
        val n8 = n4 + listOf(Offset(-1, -1), Offset(-1, 0), Offset(-1, 1), Offset(0, -1))
        val queue = ArrayDeque<Int>()
        for (i in cells.indices) {
            val (r, c) = i / cols to i % cols
            if (lines[r][c] == '@') {
                cells[i].isPaper = true
                n4.map { (dr, dc) -> Offset(dr + r, dc + c) }
                    .filter { (nr, nc) -> nr in 0..<rows && nc in 0..<cols && lines[nr][nc] == '@' }
                    .forEach { (nr, nc) -> cells[i].counts++; cells[nr * cols + nc].counts++ }
            }
        }
        var part1Count = 0
        cells.forEachIndexed { index, cell ->
            if (cell.isPaper && cell.counts < 4) {
                queue.add(index)
                cell.removed = true
                part1Count++
            }
        }
        var part2Removed = part1Count
        while (!queue.isEmpty()) {
            val currentIdx = queue.poll()
            val (r, c) = currentIdx / cols to currentIdx % cols
            n8.map { (dr, dc) -> Offset(dr + r, dc + c) }
                .filter { (nr, nc) -> nr in 0..<rows && nc in 0..<cols }
                .map { (nr, nc) -> nr * cols + nc }
                .filter { i -> cells[i].isPaper && !cells[i].removed }
                .forEach { i ->
                    cells[i].counts--
                    if (cells[i].counts < 4) {
                        cells[i].removed = true
                        queue.add(i)
                        part2Removed++
                    }
                }
        }
        return part1Count to part2Removed
    }
    val (p1Test, p2Test) = partOneAndTwo(readInput("Day04_test"))
    check(p1Test == 13) { "Part 1 Test: $p1Test (Expected: 13)" }
    check(p2Test == 43) { "Part 2 Test: $p2Test (Expected: 43)" }
    val input = readInput("Day04")
    val (result, time) = (1..10).map { measureTimedValue { partOneAndTwo(input) } }.minBy { it.duration }
    println("Part 1: ${result.first}")
    println("Part 2: ${result.second}")
    println("Time: $time")
}