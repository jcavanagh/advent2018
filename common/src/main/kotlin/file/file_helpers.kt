package common.file

import java.io.File

/**
 * Helper transform for things that just want the string value
 */
fun noopTransform(line: String): String {
  return line
}

/**
 * Reads a file, and returns a List of its lines as Anything
 */
fun <T: Any> readLines(file: String, lineTransform: (String) -> T): List<T> {
  val lines = mutableListOf<T>()

  File(file).forEachLine { lines.add(lineTransform(it)) }

  return lines
}

fun readLines(file: String): List<String> {
  return readLines(file, ::noopTransform)
}