package me.cassayre.florian.cursorsio

import java.awt.Color
import java.net.URI
import java.nio.{ByteBuffer, ByteOrder}

import me.cassayre.florian.cursorsio.elements._
import me.cassayre.florian.cursorsio.packets._
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake

class CursorsWebsocketClient(uri: URI) extends WebSocketClient(uri) {

  private var listeners: Set[CursorsListener] = Set()

  def addListener(listener: CursorsListener): Unit = {
    listeners += listener
  }

  override def onClose(code: Int, reason: String, remote: Boolean): Unit = {
    listeners.foreach(_.onClose(code, reason, remote))
  }

  override def onMessage(buffer: ByteBuffer): Unit = {
    buffer.order(ByteOrder.LITTLE_ENDIAN) // Very important, bytes are little-endian ordered

    //println(bytesToString(buffer.array()).mkString)

    val id = buffer.get() & 0xff
    val packet = id match {
      case 0x00 => // Player id
        val playerId = buffer.getInt()

        PacketClientId(playerId)

      case 0x01 => // Board update (cursors)
        val updatesCount = buffer.getShort() & 0xffff
        var cursors: Map[Int, (Int, Int)] = Map()
        for (i <- 0 until updatesCount) {
          val cursorId = buffer.getInt()
          val to = (buffer.getShort() & 0xffff, buffer.getShort() & 0xffff)
          cursors += cursorId -> to
        }

        val clicksCount = buffer.getShort() & 0xffff
        var clicks: List[(Int, Int)] = List()
        for (i <- 0 until clicksCount) {
          val x = buffer.getShort() & 0xffff
          val y = buffer.getShort() & 0xffff
          clicks = (x, y) :: clicks
        }

        val removeCount = buffer.getShort() & 0xffff
        var removedElements: List[Int] = List()
        for (i <- 0 until removeCount) {
          val elementId = buffer.getInt()
          removedElements = elementId :: removedElements
        }

        val addCount = buffer.getShort() & 0xffff
        var addedElements: Map[Int, CursorsElement] = Map()
        for (i <- 0 until addCount) {
          val elementId = buffer.getInt()
          val element = readElement(buffer)
          addedElements += elementId -> element
        }

        val drawingsCount = buffer.getShort() & 0xffff
        var drawings: List[(Int, Int, Int, Int)] = List()
        for (i <- 0 until drawingsCount) {
          val (fromX, fromY) = (buffer.getShort() & 0xffff, buffer.getShort() & 0xffff)
          val (toX, toY) = (buffer.getShort() & 0xffff, buffer.getShort() & 0xffff)
          drawings = (fromX, fromY, toX, toY) :: drawings
        }

        val totalPlayers = buffer.getInt()

        PacketBoardUpdate(cursors, clicks, removedElements, addedElements, drawings, totalPlayers)

      case 0x04 => // Initial board content

        val (spawnX, spawnY) = (buffer.getShort() & 0xffff, buffer.getShort() & 0xffff)
        val elementsCount = buffer.getShort() & 0xffff
        var elements: Map[Int, CursorsElement] = Map()
        for (i <- 0 until elementsCount) {
          val elementId = buffer.getInt()
          val element = readElement(buffer)
          elements += elementId -> element
        }

        val lastTeleportId = buffer.getInt()

        PacketBoardContent(spawnX, spawnY, elements, lastTeleportId)

      case 0x05 => // Teleport player
        val x = buffer.getShort() & 0xffff
        val y = buffer.getShort() & 0xffff
        val lastTeleportId = buffer.getInt()

        PacketTeleport(x, y, lastTeleportId)

      // Case else: match error
    }

    listeners.foreach(_.onPacket(packet))
  }


  private def readElement(buffer: ByteBuffer): CursorsElement = {
    def readBoolean(): Boolean = buffer.get() match { case 0 => false case 1 => true }
    def readDimensions(): (Int, Int, Int, Int) = (buffer.getShort() & 0xffff, buffer.getShort() & 0xffff, buffer.getShort() & 0xffff, buffer.getShort() & 0xffff)
    def readColor(): Color = new Color(buffer.getInt())
    def readString(): String = {
      var string = ""
      var c = buffer.get()
      while (c != 0) {
        string += c.toChar
        c = buffer.get()
      }
      string
    }

    val elementType = buffer.get() & 0xff
    val element = elementType match {
      case 0xff => // Nothing
        EmptyElement()
      case 0x00 => // Text
        val x = buffer.getShort() & 0xffff
        val y = buffer.getShort() & 0xffff
        val size = buffer.get() & 0xff
        val isCentered = readBoolean()
        val text: String = readString()
        TextElement(x, y, size, isCentered, text)
      case 0x01 => // Wall
        val (x, y, width, height) = readDimensions()
        val color = readColor()
        WallElement(x, y, width, height, color)
      case 0x02 => // Coloured area
        val (x, y, width, height) = readDimensions()
        val isBad = readBoolean()
        AreaElement(x, y, width, height, isBad)
      case 0x03 => // Pressure plate
        val (x, y, width, height) = readDimensions()
        val count = buffer.getShort() & 0xffff
        val color = readColor()
        PlateElement(x, y, width, height, count, color)
      case 0x04 => // Button
        val (x, y, width, height) = readDimensions()
        val count = buffer.getShort() & 0xffff
        val color = readColor()
        ButtonElement(x, y, width, height, count, color)
      case 0x05 => // ???
        val x = buffer.getShort() & 0xffff
        val y = buffer.getShort() & 0xffff
        BackgroundElement(x, y)
    }

    element
  }

  override def onOpen(handshakedata: ServerHandshake): Unit = {
    listeners.foreach(_.onOpen())
  }

  override def onError(ex: Exception): Unit = {
    listeners.foreach(_.onError(ex))
  }

  override def onMessage(message: String): Unit = {} // String messages are simply ignored

  private def bytesToString(bytes : Array[Byte]): Array[String] = bytes.map(b => "%02X".format(b & 0xff)) // Debug only
}
