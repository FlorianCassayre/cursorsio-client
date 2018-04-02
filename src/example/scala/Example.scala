import me.cassayre.florian.cursorsio._
import me.cassayre.florian.cursorsio.packets._

object Example extends App {

  val ip = "ws://192.81.129.177:2828" // There is currently only one instance running

  val client = CursorsClient(ip)
  client.addListener(new CursorsListener {
    override def onError(ex: Exception): Unit = {
      ex.printStackTrace()
    }

    override def onClose(code: Int, reason: String, remote: Boolean): Unit = {
      println(s"""closed: code=$code, reason="$reason", remote=$remote""")
    }

    override def onOpen(): Unit = {
      println("opened")
    }

    override def onPacket(packet: CursorsServerPacket): Unit = {
      println(s"packet: $packet")

      packet match {
        case PacketClientId(_) =>
          client.sendPacket(PacketMove(300, 175, 2)) // Example that completes the first level
        case _ =>
      }
    }
  })

  client.connect()

}
