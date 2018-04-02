package me.cassayre.florian.cursorsio

import me.cassayre.florian.cursorsio.packets._

trait CursorsListener {

  def onOpen(): Unit

  def onClose(code: Int, reason: String, remote: Boolean): Unit

  def onError(ex: Exception): Unit

  def onPacket(packet: CursorsServerPacket): Unit

}
