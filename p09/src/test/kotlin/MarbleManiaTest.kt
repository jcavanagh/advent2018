package p09.test

import p09.MarbleGame
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class MarbleManiaTest {
  @Test fun testGame() {
    //10 players; last marble is worth 1618 points: high score is 8317
    //13 players; last marble is worth 7999 points: high score is 146373
    //17 players; last marble is worth 1104 points: high score is 2764
    //21 players; last marble is worth 6111 points: high score is 54718
    //30 players; last marble is worth 5807 points: high score is 37305
    val testData = listOf(
      Triple(10, 1618, 8317),
      Triple(13, 7999, 146373),
      Triple(17, 1104, 2764),
      Triple(21, 6111, 54718),
      Triple(30, 5807, 37305)
    )

    for(datum in testData) {
      try {
        val winner = MarbleGame(datum.first, datum.second).play()
        assertEquals(datum.third, winner.second, "Game result did not meet expectations")
      } catch (e: Exception) {
        fail("Game with $datum threw: $e : ${e.stackTrace}")
      }
    }
  }

  @Test fun testTurn() {
    val points = listOf(
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 32, 0, 0
    )

    val currentMarble = listOf(
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
      21, 22, 19, 24, 25
    )

    val circleState = listOf(
      listOf(0, 1),
      listOf(0, 2, 1),
      listOf(0, 2, 1, 3),
      listOf(0, 4, 2, 1, 3),
      listOf(0, 4, 2, 5, 1, 3),
      listOf(0, 4, 2, 5, 1, 6, 3),
      listOf(0, 4, 2, 5, 1, 6, 3, 7),
      listOf(0, 8, 4, 2, 5, 1, 6, 3, 7),
      listOf(0, 8, 4, 9, 2, 5, 1, 6, 3, 7),
      listOf(0, 8, 4, 9, 2, 10, 5, 1, 6, 3, 7),
      listOf(0, 8, 4, 9, 2, 10, 5, 11, 1, 6, 3, 7),
      listOf(0, 8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 3, 7),
      listOf(0, 8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 7),
      listOf(0, 8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7),
      listOf(0, 8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 9, 19, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 9, 19, 2, 20, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 9, 19, 2, 20, 10, 21, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 9, 19, 2, 20, 10, 21, 5, 22, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 19, 2, 20, 10, 21, 5, 22, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 19, 2, 24, 20, 10, 21, 5, 22, 11, 1, 12, 6, 13, 3, 14, 7, 15),
      listOf(0, 16, 8, 17, 4, 18, 19, 2, 24, 20, 25, 10, 21, 5, 22, 11, 1, 12, 6, 13, 3, 14, 7, 15)
    )

    assertEquals(points.size, circleState.size, "Fix yo damn test data")
    assertEquals(points.size, currentMarble.size, "Fix yo damn test data")

    val mg = MarbleGame(9, 24)
    for(idx in 0..(points.size - 1)) {
      val turnPoints = mg.turn()
      assertEquals(points[idx], turnPoints, "Points for turn $idx not as expected")
      assertEquals(circleState[idx], mg.circle.map { it.value }, "Circle state for turn $idx not as expected")
      assertEquals(circleState[idx].reversed(), mg.circle.mapFromTail { it.value }, "Reversed circle state for turn $idx not as expected")
    }

    val mg2 = MarbleGame(9, 24)
    val score = mg2.play()

    assertEquals(32, score.second)
  }
}
