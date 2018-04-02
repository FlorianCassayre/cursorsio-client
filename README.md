cursorsio-client
================

![Travis](https://img.shields.io/travis/FlorianCassayre/cursorsio-client.svg)
![License](https://img.shields.io/github/license/FlorianCassayre/cursorsio-client.svg)

## Setup

### SBT

In your `build.sbt` file, add the following resolver:

    resolvers += "FlorianCassayre-cursorsio-client" at "https://packagecloud.io/FlorianCassayre/cursorsio-client/maven2"

and include the library as a dependency:

    libraryDependencies += "me.cassayre.florian" % "cursorsio-client_2.12" % "1.0"

### Maven

In your `pom.xml` file, add the following resolver in `repositories`:

    <repository>
      <id>FlorianCassayre-cursorsio-client</id>
      <url>https://packagecloud.io/FlorianCassayre/cursorsio-client/maven2</url>
    </repository>

and include the library as a dependency in `dependencies`:

    <dependency>
      <groupId>me.cassayre.florian</groupId>
      <artifactId>cursorsio-client_2.12</artifactId>
      <version>1.0</version>
    </dependency>

## Example

See [Example.scala](https://github.com/FlorianCassayre/cursorsio-client/blob/master/src/example/scala/Example.scala).