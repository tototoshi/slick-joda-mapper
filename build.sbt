
organization := "com.github.tototoshi"

name := "slick-joda-mapper"

crossScalaVersions := Seq("2.12.1", "2.11.8", "2.10.6")

scalaVersion := crossScalaVersions.value.head

scalacOptions ++= Seq("-deprecation", "-language:_")

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9" % "provided",
  "org.joda" % "joda-convert" % "1.8" % "provided",
  "com.h2database" % "h2" % "[1.4,)" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.slick" %% "slick" % "3.2.0-M2" % "provided"
)

initialCommands in (console) += """
  import com.github.tototoshi.slick.JodaSupport._
  import org.joda.time._
  import java.sql._
"""

publishMavenStyle := true
publishTo := {
  if ((version in ThisBuild).value.trim().endsWith("SNAPSHOT")) {
    Some(Opts.resolver.sonatypeSnapshots)
  } else {
    Some(Opts.resolver.sonatypeStaging)
  }
}

publishArtifact in Test := false

pomExtra := {
  <url>http://github.com/tototoshi/slick-joda-mapper</url>
  <licenses>
    <license>
      <name>Two-clause BSD-style license</name>
      <url>http://github.com/tototoshi/slick-joda-mapper/blob/master/LICENSE.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:tototoshi/slick-joda-mapper.git</url>
    <connection>scm:git:git@github.com:tototoshi/slick-joda-mapper.git</connection>
  </scm>
  <developers>
    <developer>
      <id>tototoshi</id>
      <name>Toshiyuki Takahashi</name>
      <url>http://tototoshi.github.com</url>
    </developer>
  </developers>
}
