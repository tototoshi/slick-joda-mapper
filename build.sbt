import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._

def testContainerVersion = "1.15.2"

lazy val `slick-joda-mapper` = project.in(file("."))
  .settings(scalariformSettings)
  .settings(publishingSettings)
  .settings(
    name := "slick-joda-mapper",
    organization := "com.github.tototoshi",
    version := "2.5.0",
    crossScalaVersions ++= Seq("2.11.12", "2.12.15", "2.13.8", "3.0.2"),
    scalaVersion := "2.13.8",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-language:reflectiveCalls",
      "-language:implicitConversions",
    ),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) =>
          Seq(
            "-source",
            "3.0-migration",
          )
        case _ =>
          Nil
      }
    },
    Test / sources := {
      if (scalaBinaryVersion.value == "3") {
        val exclude = Set(
          "JodaSupportSpec.scala" // TODO slick macro
        )
        (Test / sources).value.filterNot(x => exclude(x.getName))
      } else {
        (Test / sources).value
      }
    },
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.10.13" % "provided",
      "org.joda" % "joda-convert" % "2.2.2" % "provided",
      "com.h2database" % "h2" % "2.1.210" % "test",
      "com.dimafeng" %% "testcontainers-scala" % "0.39.12" % "test",
      "mysql" % "mysql-connector-java" % "8.0.28" % "test",
      "org.postgresql" % "postgresql" % "42.3.1" % "test",
      "org.testcontainers" % "mysql" % testContainerVersion % "test",
      "org.testcontainers" % "postgresql" % testContainerVersion % "test",
      "org.slf4j" % "slf4j-simple" % "1.7.35" % "test",
      "org.scalatest" %% "scalatest" % "3.2.11" % "test",
      "com.typesafe.slick" %% "slick" % "3.3.3" % "provided" cross CrossVersion.for3Use2_13
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
  Test / publishArtifact := false,
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
