import p13.Cart
import p13.FACING
import p13.Node
import kotlin.test.Test
import kotlin.test.assertEquals

class MineCartMadnessTest {
  @Test fun testRelativeFacing() {
    val testData = listOf(
      Triple(FACING.UP, -1, FACING.LEFT),
      Triple(FACING.UP, 0, FACING.UP),
      Triple(FACING.UP, 1, FACING.RIGHT),

      Triple(FACING.RIGHT, -1, FACING.UP),
      Triple(FACING.RIGHT, 0, FACING.RIGHT),
      Triple(FACING.RIGHT, 1, FACING.DOWN),

      Triple(FACING.DOWN, -1, FACING.RIGHT),
      Triple(FACING.DOWN, 0, FACING.DOWN),
      Triple(FACING.DOWN, 1, FACING.LEFT),

      Triple(FACING.LEFT, -1, FACING.DOWN),
      Triple(FACING.LEFT, 0, FACING.LEFT),
      Triple(FACING.LEFT, 1, FACING.UP)
    )

    for(datum in testData) {
      assertEquals(datum.third, datum.first.relative(datum.second), "Turn ${datum.second} from ${datum.first} not as expected")
    }
  }

  @Test fun testCartTurning() {
    val cart = Cart(FACING.UP, Node(0, 0, '+'))
    cart.turn()
    assertEquals(FACING.LEFT, cart.facing)
    cart.turn()
    assertEquals(FACING.LEFT, cart.facing)
    cart.turn()
    assertEquals(FACING.UP, cart.facing)
    cart.turn()
    assertEquals(FACING.LEFT, cart.facing)

    val cart2 = Cart(FACING.DOWN, Node(0, 0, '+'))
    cart2.turn()
    assertEquals(FACING.RIGHT, cart2.facing)
    cart2.turn()
    assertEquals(FACING.RIGHT, cart2.facing)
    cart2.turn()
    assertEquals(FACING.DOWN, cart2.facing)
    cart2.turn()
    assertEquals(FACING.RIGHT, cart2.facing)
  }
}