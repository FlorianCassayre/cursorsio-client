package me.cassayre.florian.cursorsio

import java.net.URI
import java.nio.{ByteBuffer, ByteOrder}

import me.cassayre.florian.cursorsio.packets._

class CursorsClient private(websocket: CursorsWebsocketClient) {

  /**
    * Connects to the websocket host.
    */
  def connect(): Unit = {
    websocket.connect()
  }

  /**
    * Adds a packet listener to the connection.
    * Should be called before `connect()`.
    * @param listener the listener to be added
    */
  def addListener(listener: CursorsListener): Unit = {
    websocket.addListener(listener)
  }

  /**
    * Asynchronously sends the packet.
    * No reordering is done.
    * @param packet the packet to be sent
    */
  def sendPacket(packet: CursorsClientPacket): Unit = this.synchronized {
    val buffer = createBuffer(9)
    packet match {
      case PacketMove(x, y, lastTeleportId) =>
        buffer.put(1.toByte)
        buffer.putShort(x.toShort)
        buffer.putShort(y.toShort)
        buffer.putInt(lastTeleportId)
      case PacketClick(x, y, lastTeleportId) =>
        buffer.put(2.toByte)
        buffer.putShort(x.toShort)
        buffer.putShort(y.toShort)
        buffer.putInt(lastTeleportId)
      case PacketDraw(fromX, fromY, toX, toY) =>
        buffer.put(3.toByte)
        buffer.putShort(fromX.toShort)
        buffer.putShort(fromY.toShort)
        buffer.putShort(toX.toShort)
        buffer.putShort(toY.toShort)
    }
    sendBuffer(buffer)
  }

  private def createBuffer(size: Int): ByteBuffer = {
    val buffer = ByteBuffer.allocate(size)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
  }

  private def sendBuffer(buffer: ByteBuffer): Unit = {
    val array = buffer.array().take(buffer.position())
    websocket.send(array)
  }

}

object CursorsClient {

  /**
    * Creates a new cursors.io client.
    * @param uri the URI to connect to
    * @return the client
    */
  def apply(uri: URI): CursorsClient = new CursorsClient(new CursorsWebsocketClient(uri))

  /**
    * Creates a new cursors.io client.
    * @param url the URL to connect to
    * @return the client
    */
  def apply(url: String): CursorsClient = this(new URI(url))

}