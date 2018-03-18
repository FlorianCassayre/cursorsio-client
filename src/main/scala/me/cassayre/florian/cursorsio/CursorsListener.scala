package me.cassayre.florian.cursorsio

trait CursorsListener {

  def onOpen(): Unit

  def onClose(code: Int, reason: String, remote: Boolean): Unit

  def onError(ex: Exception): Unit

  def onPacket(packet: CursorsServerPacket): Unit

}
