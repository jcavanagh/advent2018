package p08

import common.file.readLines
import java.util.*

class Node(
  val metadata: MutableList<Int> = mutableListOf(),
  val children: MutableList<Node> = mutableListOf()
) {
  fun sum(): Int {
    return children.fold(metadata.sum()) { all, node ->
      all + node.sum()
    }
  }

  fun value(): Int {
    if(children.isEmpty()) {
      return metadata.sum()
    } else {
      return metadata.fold(0) { all, meta ->
        //Find the node at the meta index, and value it
        val node = children.getOrNull(meta - 1)
        all + (node?.value() ?: 0)
      }
    }
  }
}

fun parseLicense(items: List<Int>) : Node {
  data class ParseItem(val node: Node, var numChildren: Int, var numMeta: Int)

  val head = ParseItem(Node(), items[0], items[1])
  val stack = LinkedList<ParseItem>()
  stack.add(head)

  var pointer = 2

  while(pointer < items.size) {
    val current = stack.pop()
    if (current.node.children.size < current.numChildren) {
      val parseItem = ParseItem(Node(), items[pointer], items[pointer + 1])
      current.node.children.add(parseItem.node)
      stack.push(current)
      stack.push(parseItem)
      pointer += 2
    } else if(current.node.metadata.isEmpty()) {
      current.node.metadata.addAll(items.subList(pointer, pointer + current.numMeta))
      pointer += current.numMeta
    }
  }

  return head?.node!!
}

fun main() {
  val licenseTree = parseLicense(readLines("input.txt")[0].split(" ").map { it.toInt() })

  println("Sum of meta: ${licenseTree.sum()}")
  println("Root value: ${licenseTree.value()}")
}
