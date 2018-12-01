package p01

import common.file.*

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

  println("First Repeat: $foundVal")
}

fun main(args: Array<String>) {
  val lines = readLines("src/p01/input.txt") { line: String -> line.toInt(10) }

  sum(lines)
  firstRepeat(lines)
}