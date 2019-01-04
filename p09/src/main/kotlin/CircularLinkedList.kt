package p09

class CircularLinkedList<T> {
  class Node<T> {
    var value: T
    var next: Node<T>
    var prev: Node<T>

    constructor(value: T, next: Node<T>? = null, prev: Node<T>? = null) {
      this.value = value
      this.next = next ?: this
      this.prev = prev ?: this
    }
  }

  var head: Node<T>
  var tail: Node<T>
  var size: Int

  constructor(init: T) {
    val node = Node(init)
    head = node
    tail = node
    size = 1
  }

  fun add(item: T, at: Node<T> = tail): Node<T> {
    val newNode = Node(item, at.next, at)

    at.next.prev = newNode
    at.next = newNode

    size++
    if(at == tail) {
      tail = newNode
    }

    return newNode
  }

  fun remove(node: Node<T>) {
    if(size == 1) {
      return
    }

    node.prev.next = node.next
    node.next.prev = node.prev

    if(node == head) {
      head = node.next
    }

    if(node == tail) {
      tail = node.prev
    }

    size--
  }

  fun map(lambda: (Node<T>) -> T): List<T> {
    val list = mutableListOf<T>()
    var next = head
    for(i in 0..(size - 1)) {
      list.add(lambda(next))
      next = next.next
    }
    return list
  }

  fun mapFromTail(lambda: (Node<T>) -> T): List<T> {
    val list = mutableListOf<T>()
    var next = tail
    for(i in 0..(size - 1)) {
      list.add(lambda(next))
      next = next.prev
    }
    return list
  }
}