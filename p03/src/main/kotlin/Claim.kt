package p03

class Claim {
  val id: Int
  val x: Int
  val y: Int
  val dx: Int
  val dy: Int
  val maxX: Int
  val maxY: Int

  constructor(claimStr: String) {
    val split = claimStr.split(Regex("""(,|x|\s*@\s*|\s*:\s*)"""))

    this.id = split[0].substring(1).toInt()
    this.x = split[1].toInt()
    this.y = split[2].toInt()
    this.dx = split[3].toInt()
    this.dy = split[4].toInt()
    this.maxX = this.x + this.dx
    this.maxY = this.y + this.dy
  }

  override fun hashCode(): Int = this.id
}