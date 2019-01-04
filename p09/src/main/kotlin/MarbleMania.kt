package p09

class MarbleGame {
  var currentNode: CircularLinkedList.Node<Int>
  var currentMarble = 1
  var circle = CircularLinkedList(0)
  var scores = mutableMapOf<Int, Long>()

  private val numPlayers: Int
  private val numMarbles: Int

  constructor(numPlayers: Int, numMarbles: Int) {
    this.numPlayers = numPlayers
    this.numMarbles = numMarbles
    currentNode = circle.head
  }

  private fun move(relativeMove: Int) {
    for(i in 1..Math.abs(relativeMove)) {
      currentNode = when(relativeMove < 0) {
        true -> currentNode.prev
        false -> currentNode.next
      }
    }
  }

  //Executes a turn, returns the points for that turn
  fun turn(): Int {
    var points = 0
    if(currentMarble % 23 == 0) {
      move(-7)
      val removedValue = currentNode.value
      circle.remove(currentNode)
      currentNode = currentNode.next
      points = currentMarble + removedValue
    } else {
      move(1)
      currentNode = circle.add(currentMarble, currentNode)
    }

    currentMarble++

    return points
  }

  fun play(): Pair<Int, Long> {
    var player = 0
    while(currentMarble <= numMarbles) {
      val points = turn()
      scores.putIfAbsent(player, 0)
      scores[player] = scores[player]!! + points

     //Loop players
      player++
      player %= numPlayers
    }

    return scores.maxBy { it.value }?.toPair() ?: Pair(-1, -1L)
  }
}

fun main() {
  val players = 429
  val lastMarbleValue = 70901

  val winner = MarbleGame(players, lastMarbleValue).play()
  println("Winner: Player ${winner.first}, with ${winner.second} points")

  val winner2 = MarbleGame(players, lastMarbleValue * 100).play()
  println("Winner (100x): Player ${winner2.first}, with ${winner2.second} points")
}