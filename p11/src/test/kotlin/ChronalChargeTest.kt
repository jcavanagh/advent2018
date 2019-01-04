package p11

import kotlin.test.Test
import kotlin.test.assertEquals

class ChronalChargeTest {
  @Test fun testCellPower() {
    val testData = listOf(
      Triple(Pair(122, 79), 57, -5),
      Triple(Pair(217, 196), 39, 0),
      Triple(Pair(101, 153), 71, 4)
    )

    for(datum in testData) {
      val grid = Grid(datum.second)
      val cell = grid[datum.first.first, datum.first.second]
      assertEquals(datum.third, cell.power, "Power not as expected for point: ${datum.first}")
    }
  }
}