package p02

import common.file.readLines

fun hamming(str1: String, str2: String): Int {
  if(str1 !== str2) {
    // Hamming distance
    val zipped = str1.asIterable().zip(str2.asIterable())
    return zipped.sumBy { if (it.first == it.second) 0 else 1 }
  }

  return -1
}

/**
 * Returns id1, id2, and the common characters as a List
 */
fun similarId(ids: List<String>): List<String> {
  fun find(): List<String> {
    ids.forEach { id1 ->
      ids.forEach { id2 ->
        if (hamming(id1, id2) == 1) {
          return listOf(id1, id2)
        }
      }
    }

    return listOf("", "")
  }

  val (first, second) = find()

  var common = first.asIterable().filterIndexed { idx, chr ->
    second[idx] == chr
  }.joinToString("")
  return listOf(first, second, common)
}

fun checksum(ids: List<String>): Int {
  var twos = 0
  var threes = 0

  ids.forEach {
    val counts = it.groupingBy { it }.eachCount()

    val values = counts.values
    if(values.contains(2)) {
      twos++
    }

    if(values.contains(3)) {
      threes++
    }
  }

  return twos * threes
}

fun main(args: Array<String>) {
  val lines = readLines("input.txt")
  val checksum = checksum(lines)
  val (id1, id2, common) = similarId(lines)

  println("Checksum: $checksum")
  println("Similar ID: $id1 (${id1.length}), $id2 (${id2.length}) -> $common (${common.length})")
}