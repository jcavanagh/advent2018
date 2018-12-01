package p01

import java.io.File

fun readFile(file: String): List<Int> {
  val lines = mutableListOf<Int>()

  File(file).forEachLine { line: String -> lines.add(line.toInt(10)) }

  return lines
}

fun sum(lines: List<Int>) {
  println("Sum: ${lines.sum()}")
}

fun firstRepeat(lines: List<Int>) {
  val memo = mutableMapOf<Int, Boolean>()
  var found = false
  var foundVal = 0
  var init = 0

  while(!found) {
    init = lines.fold(init) {
      acc: Int, line: Int ->
        if(!found and memo.getOrDefault(acc, false)) {
          found = true
          foundVal = acc
        }

        memo.set(acc, true)
        acc + line
    }
  }

  println("First Repeat: ${foundVal}")
}

fun main(args: Array<String>) {
  val lines = readFile("src/p01/input.txt")

  sum(lines)
  firstRepeat(lines)
}