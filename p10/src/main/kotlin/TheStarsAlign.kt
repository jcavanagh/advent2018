package p10

import common.file.readLines

class Point(var position: Pair<Int, Int>, val velocity: Pair<Int, Int>) {
  fun tick() {
    position = Pair(
      position.first + velocity.first,
      position.second + velocity.second
    )
  }
}

fun parse(line: String) : Point {
  val matches = Regex("""position=<(.+),(.+)> velocity=<(.+),(.+)>""").matchEntire(line)
  val posX = matches?.groups?.get(1)?.value!!.trim().toInt()
  val posY = matches.groups.get(2)?.value!!.trim().toInt()
  val velX = matches?.groups?.get(3)?.value!!.trim().toInt()
  val velY = matches.groups.get(4)?.value!!.trim().toInt()

  return Point(Pair(posX, posY), Pair(velX, velY))
}

fun inWindow(points: List<Point>, maxWindow: Pair<Int, Int>): Boolean {
  return points.all { point ->
    point.position.first > 0 &&
    point.position.first < maxWindow.first &&
    point.position.second > 0 &&
    point.position.second < maxWindow.second
  }
}

fun render(points: List<Point>, maxWindow: Pair<Int, Int>) {
  val display = mutableListOf<MutableList<Char>>()

  for(i in 0..maxWindow.second) {
    val line = mutableListOf<Char>()
    for(j in 0..maxWindow.first) {
      line.add('.')
    }
    display.add(line)
  }

  for(point in points) {
    display[point.position.second][point.position.first] = '#'
  }

  for(i in 0..(display.size - 1)) {
    println(display[i].joinToString(""))
  }

  Thread.sleep(1000)
}

fun main(args: Array<String>) {
  val points = readLines("input.txt").map { parse(it) }
  val maxWindow = Pair(275, 125)

  var ticks = 0
  var wasInWindow = false
  while(true) {
    points.forEach { it.tick() }
    ticks++
    if(inWindow(points, maxWindow)) {
      wasInWindow = true
      println("********** TICK ${ticks} ********** ")
      render(points, maxWindow)
      continue
    }

    if(wasInWindow) { break }
  }
}