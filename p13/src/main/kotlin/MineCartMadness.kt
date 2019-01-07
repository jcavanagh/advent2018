package p13

import common.file.readLines
import java.lang.IndexOutOfBoundsException

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
    val _node = node ?: throw Exception("Cart's off the rails, RIP")

    if (_node.isIntersection()) {
      turn()
    }

    val newPos = _node.nextFrom(facing)
    node = newPos.first
    facing = newPos.second
  }
}

class Node(
  val x: Int,
  val y: Int,
  private var originalChar: Char
) {
  var up: Node? = null
  var right: Node? = null
  var down: Node? = null
  var left: Node? = null

  init {
    if(listOf('<', '>').contains(originalChar)) {
      originalChar = '-'
    }

    if(listOf('^', 'v').contains(originalChar)) {
      originalChar = '|'
    }
  }

  fun initialize(up: Node?, right: Node?, down: Node?, left: Node?) {
    when(originalChar) {
      '-' -> {
        this.left = left
        this.right = right
      }
      '|' -> {
        this.up = up
        this.down = down
      }
      '/', '\\' -> {
        if(up?.originalChar != '-') { this.up = up }
        if(right?.originalChar != '|') { this.right = right }
        if(left?.originalChar != '|') { this.left = left }
        if(down?.originalChar != '-') { this.down = down }
      }
      '+' -> {
        this.up = up
        this.right = right
        this.down = down
        this.left = left
      }
    }
  }

  fun isIntersection(): Boolean {
    return up != null && right != null && down != null && left != null
  }

  fun nextFrom(facing: FACING): Pair<Node, FACING> {
    class NoPathException(facing: FACING) : Throwable() {
      override val message = "No path from node ($x, $y, $originalChar) with facing: $facing (int: ${isIntersection()})"
    }

    return when(isIntersection()) {
      true -> {
        //Move the direction we are facing
        val newNode = when (facing) {
          FACING.UP -> up!!
          FACING.DOWN -> down!!
          FACING.RIGHT -> right!!
          FACING.LEFT -> left!!
        }
        Pair(newNode, facing)
      }
      false ->
        when(originalChar) {
          '-' ->
            when (facing) {
              FACING.LEFT -> Pair(left!!, FACING.LEFT)
              FACING.RIGHT -> Pair(right!!, FACING.RIGHT)
              else -> throw NoPathException(facing)
            }
          '|' ->
            when (facing) {
              FACING.UP -> Pair(up!!, FACING.UP)
              FACING.DOWN -> Pair(down!!, FACING.DOWN)
              else -> throw NoPathException(facing)
            }
          '/' ->
            when (facing) {
              FACING.UP -> Pair(right!!, FACING.RIGHT)
              FACING.RIGHT -> Pair(up!!, FACING.UP)
              FACING.DOWN -> Pair(left!!, FACING.LEFT)
              FACING.LEFT -> Pair(down!!, FACING.DOWN)
            }
          '\\' ->
            when (facing) {
              FACING.UP -> Pair(left!!, FACING.LEFT)
              FACING.RIGHT -> Pair(down!!, FACING.DOWN)
              FACING.DOWN -> Pair(right!!, FACING.RIGHT)
              FACING.LEFT -> Pair(up!!, FACING.UP)
              else -> throw NoPathException(facing)
            }
          else -> throw NoPathException(facing)
        }
    }
  }
}

fun load(lines: List<String>): List<Cart> {
  val carts = mutableListOf<Cart>()

  fun makeNode(x: Int, y: Int): Node? {
    try {
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
    } catch(e : IndexOutOfBoundsException) {
      return null
    }
  }

  val nodes = mutableListOf<MutableList<Node?>>()
  val maxY = 0.until(lines.size)
  val maxX = 0.until(lines.maxBy { it.length }!!.length)
  for(y in maxY) {
    val line = mutableListOf<Node?>()
    for(x in maxX) {
      line.add(makeNode(x, y))
    }
    nodes.add(line)
  }

  //Connect nodes
  for(y in maxY) {
    val maxX = 0.until(lines[y].length)
    for (x in maxX) {
      val node = nodes[y][x]
      node?.initialize(
        if(y > 0) nodes[y - 1][x] else null, //up
        if(x < nodes[y].size - 1) nodes[y][x + 1] else null, //right
        if(y < nodes.size - 1) nodes[y + 1][x] else null, //down
        if(x > 0) nodes[y][x - 1] else null  //left
      )
    }
  }

  return carts
}

fun collide(carts: List<Cart>): List<Node?> {
  fun checkCollision(): List<Node?>? {
    val nodes = carts.map { it.node }
    val counted = nodes.groupingBy { it }.eachCount()
    val collisions = counted.filter { it.value > 1 }
    if(collisions.isNotEmpty()) {
      return collisions.map { it.key }
    }

    return null
  }

  var collision: List<Node?>? = null
  do {
    carts.forEach { it.move() }
    collision = checkCollision()
  } while(collision == null)

  return collision
}

fun main(args: Array<String>) {
  val lines = readLines("input.txt")
  val carts = load(lines)
  val collisions = collide(carts)
  collisions.forEach {
    println("Collision: (${it?.x},${it?.y})")
  }
}