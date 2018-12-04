package p03

import common.file.readLines

class Claim {
  val id: Int
  val x: Int
  val y: Int
  val dx: Int
  val dy: Int
  val maxX: Int
  val maxY: Int

  constructor(claimStr: String) {
    val split = claimStr.split(Regex("""(,|x|\s*@\s*|\s*:\s*)"""))

    this.id = split[0].substring(1).toInt()
    this.x = split[1].toInt()
    this.y = split[2].toInt()
    this.dx = split[3].toInt()
    this.dy = split[4].toInt()
    this.maxX = this.x + this.dx
    this.maxY = this.y + this.dy
  }

  override fun hashCode(): Int = this.id
}

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