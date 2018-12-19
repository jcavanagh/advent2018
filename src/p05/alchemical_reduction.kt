package p05

import common.file.readLines

fun canReact(one: Char, two: Char): Boolean {
  return one != two && one.toLowerCase() == two.toLowerCase()
}

fun react(polymer: String): String {
  return react(polymer.toMutableList())
}

fun react(polymer: MutableList<Char>): String {
  var pointer = 0
  while (pointer < polymer.size - 1) {
    if(canReact(polymer[pointer], polymer[pointer + 1])) {
      //Remove both, step back to check for chain reactions
      polymer.removeAt(pointer)
      polymer.removeAt(pointer)
      if(pointer != 0) {
        pointer--
      }
    } else {
      pointer++
    }
  }

  return polymer.joinToString("")
}

fun reactFilter(polymer: String): List<String> {
  val filters = 'a'..'z'

  return filters.map { filter ->
    var pList = polymer.toMutableList()
    pList.removeIf {
      it.toLowerCase() == filter
    }

    react(pList)
  }
}

fun main() {
  val polymer = readLines("src/p05/input.txt")[0]
  val final = react(polymer)
  val filtered = reactFilter(polymer)
  val bestFilter = filtered.minBy { it.length }

  println("Final polymer: $final")
  println("Final polymer size: ${final.length}")
  println("Best filtered polymer: ${bestFilter?.length}")
}