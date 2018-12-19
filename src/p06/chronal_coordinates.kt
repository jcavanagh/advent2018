package p06

import common.file.readLines
import java.awt.Point

fun manhattan(a: Point, b: Point): Int {
  return Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
}

fun edges(coords: List<Point>): List<Point> {
  //Find the edges - all these have "infinite" area
  val byX = coords.sortedBy { it.x }
  val byY = coords.sortedBy { it.y }

  val minXEdges = byX.filter { it.x == byX.first().x }.toTypedArray()
  val maxXEdges = byX.filter { it.x == byX.last().x }.toTypedArray()
  val minYEdges = byY.filter { it.y == byY.first().y }.toTypedArray()
  val maxYEdges = byY.filter { it.y == byY.last().y }.toTypedArray()
  return listOf(*minXEdges, *maxXEdges, *minYEdges, *maxYEdges)
}

fun <T> forLocationIn(coords: List<Point>, delegate: (Point) -> T) {
  val byX = coords.sortedBy { it.x }
  val byY = coords.sortedBy { it.y }

  //Build an inner grid to analyze
  val minX = byX.first().x
  val maxX = byX.last().x
  val minY = byY.first().y
  val maxY = byY.last().y

  for(x in minX..maxX) {
    for(y in minY..maxY) {
      delegate(Point(x, y))
    }
  }
}

fun distsFrom(coords: List<Point>, point: Point): List<Pair<Point, Int>> {
  return coords.map { coord ->
    val dist = manhattan(point, coord)
    Pair(coord, dist)
  }
}

fun mostOwnedArea(coords: List<Point>): Pair<Point, Int> {
  //Returns an owner for each point and the distance
  fun findOwner(point: Point): Pair<Point, Int>? {
    val dists = distsFrom(coords, point).sortedBy { it.second }

    //Account for multiple owners
    if(dists[0].second == dists[1].second) return null
    return dists[0]
  }

  // O(yikes)
  var owners = mutableMapOf<Point, Int>()
  forLocationIn(coords) { point ->
    val owner = findOwner(point)
    if(owner != null) {
      if(!owners.containsKey(owner.first)) {
        owners[owner.first] = 1
      } else {
        owners[owner.first] = owners[owner.first]!! + 1
      }
    }
  }

  //Exclude edges
  val edges = edges(coords)
  return owners.filterKeys { !edges.contains(it) }.entries.sortedBy { it.value }.last().toPair()
}

fun proximityWithin(coords: List<Point>, maxDist: Int): Int {
  val validLocations = mutableListOf<Point>()
  forLocationIn(coords) { point ->
    val dists = distsFrom(coords, point)

    if(dists.map { it.second }.sum() < maxDist) {
      validLocations.add(point)
    }
  }

  return validLocations.size
}

fun main() {
  val coords = readLines("src/p06/input.txt").map {
    val split = it.split(", ").map { str -> str.toInt() }
    Point(split.first(), split.last())
  }
  val mostArea = mostOwnedArea(coords)
  val mostArea10K = proximityWithin(coords, 10000)

  println("Most area: ${mostArea.first.x},${mostArea.first.y} (${mostArea.second})")
  println("Area within 10K: $mostArea10K")
}