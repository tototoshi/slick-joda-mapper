import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._

def testContainerVersion = "1.15.0"

lazy val `slick-joda-mapper` = project.in(file("."))
  .settings(scalariformSettings)
  .settings(publishingSettings)
  .settings(
    name := "slick-joda-mapper",
    organization := "com.github.tototoshi",
    version := "2.5.0",
    crossScalaVersions ++= Seq("2.11.12", "2.12.12", "2.13.4"),
    scalaVersion := "2.12.12",
    scalacOptions ++= Seq("-deprecation", "-language:_"),
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.10.8" % "provided",
      "org.joda" % "joda-convert" % "2.2.1" % "provided",
      "com.h2database" % "h2" % "[1.4,)" % "test",
      "com.dimafeng" %% "testcontainers-scala" % "0.38.7" % "test",
      "mysql" % "mysql-connector-java" % "5.1.49" % "test",
      "org.postgresql" % "postgresql" % "9.4.1212" % "test",
      "org.testcontainers" % "mysql" % testContainerVersion % "test",
      "org.testcontainers" % "postgresql" % testContainerVersion % "test",
      "org.slf4j" % "slf4j-simple" % "1.7.30" % "test",
      "org.scalatest" %% "scalatest" % "3.2.3" % "test",
      "com.typesafe.slick" %% "slick" % "3.3.3" % "provided"
    ),
    initialCommands += """
      import org.joda.time._
      import java.sql._
    """
  )

lazy val scalariformSettings = Seq(
  scalariformPreferences := scalariformPreferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
)

lazy val publishingSettings = Seq(
  publishMavenStyle := true,
  publishTo := _publishTo(version.value),
  publishArtifact in Test := false,
  pomExtra := _pomExtra
)

def _publishTo(v: String) = {
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

lazy val _pomExtra =
  <url>http://github.com/tototoshi/slick-joda-mapper</url>
    <licenses>
      <license>
        <name>Two-clause BSD-style license</name>
        <url>https://github.com/tototoshi/slick-joda-mapper/blob/master/LICENSE.txt</url>
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
        <url>https://tototoshi.github.com</url>
      </developer>
    </developers>
