package p12

import common.file.readLines

object Transitions {
  data class Transition(val match: String, val survives: Boolean)

  val transitions = loadTransitions()
  val byStrMatch = transitions.groupBy { it.match }.mapValues { it.value.first() }

  private fun loadTransitions(): List<Transition> {
    return readLines("input.txt").map {
      val spl = it.split(" => ")
      Transition(spl[0], spl[1] == "#")
    }
  }

  fun match(candidate: List<Char>): Transition? {
    return byStrMatch[candidate.joinToString("")]
  }
}

fun sumOfGenerations(plants: String, generations: Long): Long {
  fun sumOf(data: CharArray, buffer: Int): Long {
    return data.foldIndexed(0L) { idx, acc, p ->
      when (p) {
        '#' -> acc + (idx - buffer)
        else -> acc
      }
    }
  }

  //Create some buffer for the edges and just kind of hope it works out
  val buffer = 4000
  val addendum = "".padStart(buffer, '.')
  var lastGen = (addendum + plants + addendum).toCharArray()

  var lastDelta = 0L
  var sameDeltaCount = 0
  val equilibriumThreshold = 5

  for(genIdx in 1..generations) {
    val currentGenList = lastGen.copyOf()

    val startIdx = lastGen.indexOf('#') - 4
    val endIdx = lastGen.lastIndexOf('#') + 4

    for(i in startIdx..endIdx) {
      val candidate = lastGen.slice(i..(i + 4))
      val survives = Transitions.match(candidate)?.survives

      when(survives) {
        true -> currentGenList[i + 2] = '#'
        false -> currentGenList[i + 2] = '.'
        null -> currentGenList[i + 2] = '.'
      }
    }

    val currSum = sumOf(currentGenList, buffer)
    val lastSum = sumOf(lastGen, buffer)
    val delta = currSum - lastSum

    //Check for equilibrium
    if(delta == lastDelta) {
      if(sameDeltaCount >= equilibriumThreshold) {
        return currSum + (generations - genIdx) * delta
      }

      sameDeltaCount++
    } else {
      lastDelta = delta
      sameDeltaCount = 0
    }

    println(
      currentGenList.slice(startIdx..endIdx).joinToString("") +
      "(s:$startIdx, e:$endIdx, c:$currSum, l:$lastSum, d:$delta)"
    )

    lastGen = currentGenList
  }

  return sumOf(lastGen, buffer)
}

fun main(args: Array<String>) {
  val plants = ".#####.##.#.##...#.#.###..#.#..#..#.....#..####.#.##.#######..#...##.#..#.#######...#.#.#..##..#.#.#"

  val generations = 100L
  val sum = sumOfGenerations(plants, generations)
  print("Sum of plant indices at gen 20: $sum")

  val generations50B = 50000000000
  val sum50B = sumOfGenerations(plants, generations50B)
  print("Sum of plant indices at gen 50,000,000,000: $sum50B")
}