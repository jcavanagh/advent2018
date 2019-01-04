package p11

class FuelCell(val x: Int, val y: Int, gridSN: Int) {
  val rackID = x + 10
  val power = getPower(gridSN)

  private fun getPower(gridSN: Int): Int {
    var pwr = rackID * y
    pwr += gridSN
    pwr *= rackID
    pwr /= 100
    if(pwr < 100) {
      pwr = 0
    } else {
      pwr -= pwr / 10 * 10
    }
    pwr -= 5

    return pwr
  }
}

class Grid(val serialNumber: Int, val x: Int = 300) {
  private var grid: List<FuelCell> = buildGrid()

  operator fun get(atX: Int, atY: Int): FuelCell {
    return grid[atX + (atY * x)]
  }

  private fun buildGrid(): List<FuelCell> {
    return (1..(x * x)).map { FuelCell(it % x - 1, it / x, serialNumber) }
  }

  fun subGrid(offsetX: Int, offsetY: Int, side: Int): List<FuelCell> {
    var idx = offsetX + (x * offsetY)
    val cells = mutableListOf<FuelCell>()
    for(h in 1..side) {
      val sublist = grid.subList(idx, idx + side)
      cells.addAll(sublist)
      idx += x
    }

    return cells
  }

  fun maxPower(): Triple<List<List<FuelCell>>, Int, Int> {
    val init = maxPowerForSize(1)
    var maxPower = Triple(init.first, init.second, 1)

    for(i in 2..x) {
      val thisPower = maxPowerForSize(i)
      if(thisPower.second > maxPower.second) {
        maxPower = Triple(thisPower.first, thisPower.second, i)
      }
    }

    return maxPower
  }

  fun maxPowerForSize(side: Int): Pair<List<List<FuelCell>>, Int> {
    var maxCells = listOf<FuelCell>()
    var maxPower = -1

    for(i in 0..(x - side - 1)) {
      for(j in 0..(x - side - 1)) {
        val sg = subGrid(i, j, side)
        val power = sg.fold(0) { acc, fuelCell -> acc + fuelCell.power }
        if(power > maxPower) {
          maxPower = power
          maxCells = sg
        }
      }
    }

    return Pair(maxCells.chunked(side), maxPower)
  }
}

fun main(args: Array<String>) {
  val input = 7403
  val grid = Grid(input)

  val maxPower3 = grid.maxPowerForSize(3)
  val topLeft3 = maxPower3.first[0][0]
  println("Max power (3x3): ${maxPower3.second} at (${topLeft3.x},${topLeft3.y})")

  val maxPower = grid.maxPower()
  val topLeft = maxPower.first[0][0]
  val size = maxPower.third
  println("Max power: ${maxPower.second} at (${topLeft.x},${topLeft.y}) of size ${size}x$size")
}