package p13

import common.file.readLines

enum class FACING {
  UP,
  RIGHT,
  DOWN,
  LEFT;

  //Get the next facing, given a relative offset
  //e.g. turning
  fun relative(offset: Int): FACING {
    val values = FACING.values()
    val size = values.size
    val next = this.ordinal + offset

    if(next < 0) {
      return values[size - (Math.abs(offset) % size)]
    }

    return values[next % size]
  }
}

class Cart(var facing: FACING, var node: Node? = null) {
  private var turnIdx = 0
  private val turnSequence = listOf(-1, 0, 1)

  private fun turn() {
    facing = facing.relative(turnSequence[turnIdx])
    turnIdx++
    turnIdx %= turnSequence.size
  }

  fun move() {
    if(node == null) throw Exception("Cart's off the rails, RIP")

    if(node?.isIntersection() == true) {
      turn()
    }

    node = when (facing) {
      FACING.UP -> node?.up
      FACING.DOWN -> node?.down
      FACING.RIGHT -> node?.right
      FACING.LEFT -> node?.left
    }
  }
}

class Node(
  val x: Int,
  val y: Int,
  private val originalChar: Char,
  var up: Node? = null,
  var right: Node? = null,
  var down: Node? = null,
  var left: Node? = null
) {
  fun isIntersection(): Boolean {
    return up != null && right != null && down != null && left != null
  }
}

fun load(lines: List<String>): List<Cart> {
  val carts = mutableListOf<Cart>()

  fun makeNode(x: Int, y: Int): Node? {
    val char = lines[y][x]
    if(char == ' ') return null

    val node = Node(x, y, char)
    val cart = when(char) {
      '^' -> Cart(FACING.UP, node)
      '>' -> Cart(FACING.RIGHT, node)
      'v' -> Cart(FACING.DOWN, node)
      '<' -> Cart(FACING.LEFT, node)
      else -> null
    }

    if(cart != null) {
      carts.add(cart)
    }

    return node
  }

  val nodes = mutableListOf<MutableList<Node?>>()
  val maxY = 0.until(lines.size)
  val maxX = 0.until(lines[0].length)
  for(y in maxY) {
    val line = mutableListOf<Node?>()
    for(x in maxX) {
      //Each node is connected to another, so we only need to find the first one and recursively make the graph
      line.add(makeNode(x, y))
    }
    nodes.add(line)
  }

  for(y in maxY) {
    for (x in maxX) {
      val node = nodes[y][x]
      if (y > 0) {
        node?.up = nodes[x][y - 1]
      }

      if (x > 0) {
        node?.left = nodes[x - 1][y]
      }

      if (x < lines[0].length - 1) {
        node?.right = nodes[x + 1][y]
      }

      if (y < lines.size - 1) {
        node?.down = nodes[x][y + 1]
      }
    }
  }

  return carts
}

fun collide(carts: List<Cart>): Node? {
  fun checkCollision(): Node? {
    val nodes = carts.map { it.node }
    val distinct = nodes.distinctBy { "${it?.x},${it?.y}" }
    if(nodes.size != distinct.size) {
      return nodes.minus(distinct)[0]
    }
    return null
  }

  var collision: Node? = null
  do {
    carts.forEach { it.move() }
    collision = checkCollision()
  } while(collision != null)

  return collision
}

fun main(args: Array<String>) {
  val lines = readLines("input.txt")
  val carts = load(lines)
  val firstCollision = collide(carts)
  println("First collision: (${firstCollision?.x},${firstCollision?.y}")
}