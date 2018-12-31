package p07

import common.file.readLines
import java.util.*
import kotlin.NoSuchElementException

class Node<T : Comparable<T>>(var value: T, val cost: Int) : Comparable<Node<T>> {
  //Paths to other nodes
  var edges = sortedSetOf<Node<T>>()
  //Paths from other nodes
  var refs = sortedSetOf<Node<T>>()

  //A node is a root if nothing leads to it
  fun isRoot(): Boolean {
    return refs.size == 0
  }

  fun next(): Node<T>? {
    try {
      return edges.first()
    } catch(e: NoSuchElementException) {
      return null
    }
  }

  override fun compareTo(other: Node<T>): Int {
    return this.value.compareTo(other.value)
  }
}

class SleighGraph<T : Comparable<T>>(var nodes: SortedSet<Node<T>> = sortedSetOf()) {
  data class Worker<T : Comparable<T>>(val node: Node<T>, val completesAt: Int)

  fun add(value: T, depends: T, cost: (T) -> Int) {
    //This could probably be indexed
    val node = nodes.find { it.value == value } ?: Node(value, cost(value)).also { nodes.add(it) }
    val depNode = nodes.find {it.value == depends} ?: Node(depends, cost(depends)).also { nodes.add(it) }

    node.refs.add(depNode)
    depNode.edges.add(node)
  }

  fun roots(): List<Node<T>> {
    return nodes.filter { it.isRoot() }
  }

  //Traverse the list in parallel, respecting cost
  //If no workers specified, ignore cost
  fun traverse(workerCount: Int = 0, baseNodeCost: Int = 0): Pair<List<Node<T>>, Int> {
    //Count costs
    var tick = 0
    //Final list
    val ordered = mutableListOf<Node<T>>()
    //Current path choices
    val possible = sortedSetOf<Node<T>>()
    possible.addAll(this.roots())
    //Track workers
    val workers = mutableListOf<Worker<T>>()

    fun valid(): List<Node<T>> {
      return possible.filter { it.refs.minus(ordered).isEmpty() }
    }

    fun nextNode(): Node<T>? {
      val validEdges = valid()
      if(validEdges.isNotEmpty()) {
        return validEdges.first()
      }

      return null
    }

    fun startNode(node: Node<T>) {
      possible.remove(node)
    }

    fun finishNode(node: Node<T>) {
      ordered.add(node)
      possible.addAll(node.edges)
      possible.removeAll(ordered)
    }

    do {
      if(workerCount > 0) {
        //Check for workers that should be removed
        val completed = workers.filter { tick >= it.completesAt }
        completed.forEach { finishNode(it.node) }
        workers.removeAll(completed)

        val validNodes = valid()
        if(validNodes.isEmpty()) {
          //If we have no next node and no workers running, we are done
          if (workers.isEmpty()) {
            break
          }
        }

        for(node in valid()) {
          //Assign the new node to a worker, or wait for one to become available
          if (workers.size < workerCount) {
            workers.add(Worker(node, tick + baseNodeCost + node.cost))
            startNode(node)
          }
        }

        tick++
      } else {
        val node = nextNode() ?: break
        startNode(node)
        finishNode(node)
      }
    } while(valid().isNotEmpty() || (workerCount > 0 && workers.isNotEmpty()))

    return Pair(ordered, tick)
  }
}

fun loadSteps(steps: List<String>): SleighGraph<String> {
  val graph = SleighGraph<String>()
  val pattern = Regex("""^Step (\w).+step (\w).+$""")

  fun cost(value: String): Int {
    return value[0].minus('A') + 1
  }

  for(step in steps) {
    val matches = pattern.matchEntire(step)
    if(matches != null) {
      val value = matches.groups[2]?.value!!
      val depends = matches.groups[1]?.value!!
      graph.add(value, depends) { cost(it) }
    } else {
      println("Failed to parse: $step")
    }
  }

  return graph
}

fun main() {
  val graph = loadSteps(readLines("src/p07/input.txt"))
  val ordered = graph.traverse()
  val orderedWithWorkers = graph.traverse(5, 60)

  println("Base")
  println("Order: ${ordered.first.joinToString("") { it.value }}")
  println("Parallelized - 5/60")
  println("Order: ${orderedWithWorkers.first.joinToString("") { it.value }}")
  println("Duration: ${orderedWithWorkers.second}")
}
