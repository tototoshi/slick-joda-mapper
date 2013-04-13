import sbt._
import sbt.Keys._

object SlickJodaMapperBuild extends Build {

  lazy val root = Project(
    id = "slick-datetime-mapper",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "slick-joda-mapper",
      organization := "com.github.tototoshi",
      version := "0.2.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      scalacOptions ++= Seq("-deprecation", "-language:_"),
      libraryDependencies ++= Seq(
        "joda-time" % "joda-time" % "2.2",
        "org.joda" % "joda-convert" % "1.3.1",
        "com.typesafe.slick" %% "slick" % "1.0.0",
        "com.h2database" % "h2" % "[1.3,)" % "test",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
      ),
      initialCommands += """
        import com.github.tototoshi.slick.JodaSupport._
        import org.joda.time._
        import java.sql._
      """
    ) ++ publishingSettings
  )

  val publishingSettings = Seq(
    publishMavenStyle := true,
    publishTo <<= version { (v: String) => _publishTo(v) },
    publishArtifact in Test := false,
    pomExtra := _pomExtra
  )

  def _publishTo(v: String) = {
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }

  val _pomExtra =
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

