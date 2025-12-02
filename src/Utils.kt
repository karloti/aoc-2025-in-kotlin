import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Reads lines from the given input txt file.
 */
fun readInputAsText(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun readInput(name: String, separator: Char): Sequence<String> = sequence {
    Path("src/$name.txt").toFile().bufferedReader().use { reader ->
        val sb = StringBuilder()
        var nextChar = reader.read()
        while (nextChar != -1) {
            val char = nextChar.toChar()
            when {
                char == separator && sb.isNotEmpty() -> {
                    yield(sb.toString().trim())
                    sb.clear()
                }

                !char.isWhitespace() -> {
                    sb.append(char)
                }
            }
            nextChar = reader.read()
        }
        if (sb.isNotEmpty()) {
            yield(sb.toString().trim())
        }
    }
}