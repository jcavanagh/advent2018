package p09

data class Marble(val value: Int)

class MarbleGame(val numPlayers: Int, val numMarbles: Int) {
  var currentIndex = 0
  var currentMarble = 1
  var circle = mutableListOf<Marble>()
  var scores = mutableMapOf<Int, Int>()

  init {
    circle.add(Marble(0))
  }

  fun getIndex(relativeMove: Int): Int {
    if(circle.size == 0) { return 0 }
    if(circle.size == 1) { return 1 }

    var nextIndex = currentIndex + relativeMove
    if(nextIndex < 0) {
      nextIndex = circle.size - (Math.abs(nextIndex) % circle.size)
    }

    if(nextIndex > circle.size) {
      return nextIndex % circle.size
    }

    return nextIndex
  }

  fun nextMarble(add: Boolean = false) {
    currentMarble++
    if(add) {
      circle.add(Marble(currentMarble))
    }
  }

  //Executes a turn, returns the points for that turn
  fun turn(): Int {
    val next = Marble(currentMarble)

    var points = 0
    if(next.value % 23 == 0) {
      val removeIndex = getIndex(-7)
      val removed = circle.removeAt(removeIndex)
      currentIndex = removeIndex
      points = next.value + removed.value
    } else {
      val index = getIndex(2)
      circle.add(index, next)
      currentIndex = index
    }

    nextMarble()

    return points
  }

  fun play(): Pair<Int, Int> {
    var player = 0
    while(currentMarble <= numMarbles) {
      val points = turn()
      scores.putIfAbsent(player, 0)
      scores[player] = scores[player]!! + points

     //Loop players
      player++
      player %= numPlayers
    }

    return scores.maxBy { it.value }?.toPair() ?: Pair(-1, -1)
  }
}

fun main() {
  val players = 429
  val lastMarbleValue = 70901

  val winner = MarbleGame(players, lastMarbleValue).play()

  println("Winner: Player ${winner.first}, with ${winner.second} points")
}