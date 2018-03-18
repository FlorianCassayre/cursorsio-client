package me.cassayre.florian.cursorsio

import scala.io.Source

object CursorsServerFinder {

  private val baseUrl = "https://api.n.m28.io"
  private val endpoint = "cursors"

  private def urlFindEach = s"$baseUrl/endpoint/$endpoint/findEach/"

  def findServerAddress(): String = {
    val content = Source.fromURL(urlFindEach).mkString

    val regex = "\"ipv4\":\"([0-9\\.]+)\"".r

    regex.findFirstMatchIn(content).get.group(1)
  }

}
