package p09

data class Marble(val value: Int)

class MarbleGame(val numPlayers: Int, val numMarbles: Int) {
  private var currentIndex = 0
  private var currentMarble = 1
  private var circle = mutableListOf<Marble>()
  var scores = mutableMapOf<Int, Int>()

  init {
    circle.add(Marble(0))
  }

  protected fun getIndex(relativeMove: Int, allowEdge: Boolean = true): Int {
    if(circle.size < 3) {
      return 1
    }

    var nextIndex = currentIndex + relativeMove
    if(nextIndex < 0) {
      nextIndex = circle.size - Math.abs(nextIndex) - 1
    }
    if(nextIndex > circle.size) {
      return nextIndex % circle.size
    }

    return nextIndex
  }

  //Executes a turn, returns the points for that turn
  private fun turn(): Int {
    val next = Marble(currentMarble)
    if(next.value % 23 == 0) {
      val removeIndex = getIndex(-7, false)
      val removed = circle.removeAt(removeIndex)
      currentIndex = removeIndex
      return next.value + removed.value
    }

    val index = getIndex(2)
    circle.add(index, next)
    currentIndex = index
    return 0
  }

  fun play(): Pair<Int, Int> {
    var player = 0
    while(currentMarble < numMarbles) {
      val points = turn()
      scores.putIfAbsent(player, 0)
      scores[player] = scores[player]!! + points

      //Update marble count
      currentMarble++

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

  println("Winner: Player ${winner?.first}, with ${winner?.second} points")
}