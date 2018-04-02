name := "cursorsio-client"

organization := "me.cassayre.florian"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "org.java-websocket" % "Java-WebSocket" % "1.3.7"

scalaSource in Test := baseDirectory.value / "src" / "example" / "scala"

// packagecloud.io

import aether.AetherKeys._

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

aetherWagons := Seq(aether.WagonWrapper("packagecloud+https", "io.packagecloud.maven.wagon.PackagecloudWagon"))

publishTo := {
  Some("packagecloud+https" at "packagecloud+https://packagecloud.io/FlorianCassayre/cursorsio-client")
}
