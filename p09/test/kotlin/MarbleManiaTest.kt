package test.kotlin.advent2018.p09

import main.kotlin.advent2018.p09.MarbleGame
import kotlin.test.Test
import kotlin.test.assertEquals

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
    val winner = MarbleGame(datum.first, datum.second).play()
    assertEquals(datum.third, winner.second, "Game result did not meet expectations")
  }
}

@Test fun testIndexing() {
  assert(true)
}
