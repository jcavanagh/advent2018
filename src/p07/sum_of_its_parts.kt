package p07

import common.file.readLines
import java.util.*
import kotlin.NoSuchElementException

class Node<T : Comparable<T>>(var value: T) : Comparable<Node<T>> {
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
  fun add(value: T, depends: T) {
    //This could probably be indexed
    val node = nodes.find { it.value == value } ?: Node(value).also { nodes.add(it) }
    val depNode = nodes.find {it.value == depends} ?: Node(depends).also { nodes.add(it) }

    node.refs.add(depNode)
    depNode.edges.add(node)
  }

  fun roots(): List<Node<T>> {
    return nodes.filter { it.isRoot() }
  }

  fun traverse(): List<Node<T>> {
    //Final list
    val ordered = mutableListOf<Node<T>>()
    //Current path choices
    val valid = sortedSetOf<Node<T>>()
    valid.addAll(this.roots())

    do {
      val validEdges = valid.filter { it.refs.minus(ordered).isEmpty() }

      if(validEdges.isEmpty()) {
        return ordered
      }

      val node = validEdges.first()
      ordered.add(node)
      valid.addAll(node.edges)
      valid.remove(node)
      valid.removeAll(ordered)
    } while(valid.isNotEmpty())

    return ordered
  }
}

fun loadSteps(steps: List<String>): SleighGraph<String> {
  val graph = SleighGraph<String>()
  val pattern = Regex("""^Step (\w).+step (\w).+$""")

  for(step in steps) {
    val matches = pattern.matchEntire(step)
    if(matches != null) {
      graph.add(matches.groups[2]?.value!!, matches.groups[1]?.value!!)
    } else {
      println("Failed to parse: $step")
    }
  }

  return graph
}

fun main() {
  val graph = loadSteps(readLines("src/p07/input.txt"))
  val ordered = graph.traverse()

  println(ordered.map { it.value }.joinToString(""))
}
