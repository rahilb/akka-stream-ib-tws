name := "akka-stream-ib-tws-boilerplate"

version := "1.0"

scalaVersion := "2.12.1"

// Put the IWBTws.jar there
unmanagedBase := baseDirectory.value / "lib"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "com.typesafe.akka" %% "akka-stream" % "2.5.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2" % Test
)