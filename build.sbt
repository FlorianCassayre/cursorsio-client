name := "cursorsio-client"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "org.java-websocket" % "Java-WebSocket" % "1.3.7"

scalaSource in Test := baseDirectory.value / "src" / "example" / "scala"
