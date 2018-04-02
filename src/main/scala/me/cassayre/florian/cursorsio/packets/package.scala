package me.cassayre.florian.cursorsio

import me.cassayre.florian.cursorsio.elements.CursorsElement

package object packets {

  sealed abstract class CursorsPacket


  sealed abstract class CursorsServerPacket extends CursorsPacket

  case class PacketClientId(id: Int) extends CursorsServerPacket

  case class PacketBoardUpdate(cursors: Map[Int, (Int, Int)], clicks: List[(Int, Int)], removedElements: List[Int], addedElements: Map[Int, CursorsElement], drawings: List[(Int, Int, Int, Int)], totalPlayers: Int) extends CursorsServerPacket

  case class PacketBoardContent(spawnX: Int, spawnY: Int, elements: Map[Int, CursorsElement], lastTeleportId: Int) extends CursorsServerPacket

  case class PacketTeleport(x: Int, y: Int, lastTeleportId: Int) extends CursorsServerPacket


  sealed abstract class CursorsClientPacket extends CursorsPacket

  case class PacketMove(x: Int, y: Int, lastTeleportId: Int) extends CursorsClientPacket

  case class PacketClick(x: Int, y: Int, lastTeleportId: Int) extends CursorsClientPacket

  case class PacketDraw(fromX: Int, fromY: Int, toX: Int, toY: Int) extends CursorsClientPacket

}