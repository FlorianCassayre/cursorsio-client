package me.cassayre.florian.cursorsio

import java.awt.Color

package object elements {

  sealed abstract class CursorsElement

  case class EmptyElement() extends CursorsElement

  case class TextElement(x: Int, y: Int, size: Int, isCentered: Boolean, text: String) extends CursorsElement

  case class WallElement(x: Int, y: Int, width: Int, height: Int, color: Color) extends CursorsElement

  case class AreaElement(x: Int, y: Int, width: Int, height: Int, isBad: Boolean) extends CursorsElement

  case class PlateElement(x: Int, y: Int, width: Int, height: Int, count: Int, color: Color) extends CursorsElement

  case class ButtonElement(x: Int, y: Int, width: Int, height: Int, count: Int, color: Color) extends CursorsElement

  case class BackgroundElement(x: Int, y: Int) extends CursorsElement

}
