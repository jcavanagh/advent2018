package p03

import common.file.readLines

inline fun <reified T> aggregateClaims(claims: List<Claim>, init: () -> T, delegate: (T, Claim) -> T): Array<Array<T>> {
  val maxX = claims.maxBy { it.maxX }!!.maxX
  val maxY = claims.maxBy { it.maxY }!!.maxY

  val claimMap = Array(maxX) { Array(maxY) { init() } }

  for(claim in claims) {
    if(claim.dy <= 0 || claim.dx <= 0) continue

    for(x in (claim.x)..(claim.maxX - 1)) {
      for(y in (claim.y)..(claim.maxY - 1)) {
        claimMap[x][y] = delegate(claimMap[x][y], claim)
      }
    }
  }

  return claimMap
}

fun intersect(claims: List<Claim>): Int {
  val claimMap = aggregateClaims(claims, { 0 }) { current, _ ->  current + 1 }

  return claimMap.map { row -> row.filter { it >= 2 }.size }.sum()
}

fun nonIntersect(claims: List<Claim>): List<Claim> {
  val claimMap = aggregateClaims(claims, { mutableListOf<Claim>() }) { current, claim -> current.add(claim); current }
  val intersectingClaims = mutableSetOf<Claim>()

  claimMap.forEach { row ->
    row.forEach { claims ->
      if (claims.size > 1) {
        intersectingClaims.addAll(claims)
      }
    }
  }

  return claims.minus(intersectingClaims)
}

fun main() {
  val claims = readLines("src/p03/input.txt").map { Claim(it) }

  println("Intersecting claims: ${intersect(claims)}")
  println("Non-intersecting claims: ${nonIntersect(claims).map { it.id }}")
}