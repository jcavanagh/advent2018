package advent2017.p02

import common.file.*

fun quotientChecksum(sheet: List<List<Int>>): Int {
  val quotients = sheet.map {
    val sorted = it.sorted().reversed()

    for (i in 0..(sorted.size-2)) {
      for(j in (i+1)..(sorted.size-1)) {
        val mod = sorted[i] % sorted[j]

        if(mod == 0) {
          return@map sorted[i] / sorted[j]
        }
      }
    }

    0
  }

  return quotients.sum()
}

fun checksum(sheet: List<List<Int>>): Int {
  val lineChecksums = sheet.map {
    val sorted = it.sorted()
    sorted.last() - sorted.first()
  }

  return lineChecksums.sum()
}

fun main(args: Array<String>) {
  val sheet = readLines("src/advent2017.p02/input.txt") {
     line: String ->
        line.split('\t').map { it.toInt(10) }
  }

  println("Checksum: ${checksum(sheet)}")
  println("Quotient Checksum: ${quotientChecksum(sheet)}")
}