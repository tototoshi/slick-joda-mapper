# slick-joda-mapper

[![Build Status](https://travis-ci.org/tototoshi/slick-joda-mapper.png)](https://travis-ci.org/tototoshi/slick-joda-mapper)

Enables you to use joda-time with Slick.
You can persist `DateTime`, `Instant`, `LocalDateTime`, `LocalDate`, `LocalTime`, `DateTimeZone` with Slick.

# Usage


## For Slick 2.x

```scala
libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "joda-time" % "joda-time" % "2.4",
    "org.joda" % "joda-convert" % "1.6",
    "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
)
```

Import xJodaSupport(H2JodaSupport, PostgresJodaSupport, MySQLJodaSupport...) class suitable for the database you use.
For example, import `H2JodaSupport` if you are using H2Driver.

```scala
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._
```

Different drivers __can't__ be mixed, You __can't__ do the following.

```scala
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.JdbcJodaSupport._
```

Write your own `JdbcSupport` when you want to write db agnostic model class.

```scala
object PortableJodaSupport extends com.github.tototoshi.slick.GenericJodaSupport(yourAbstractDriver)

// with play-slick
object PortableJodaSupport extends com.github.tototoshi.slick.GenericJodaSupport(play.api.db.slick.Config.driver)

import PortableJodaSupport._
```

## For Slick 1.x

```scala
libraryDependencies += "com.github.tototoshi" %% "slick-joda-mapper" % "0.4.1"
```

```scala
import com.github.tototoshi.slick.JodaSupport._
```

# Example


https://github.com/tototoshi/slick-joda-mapper/blob/master/src/test/scala/com/github/tototoshi/slick/JodaSupportSpec.scala


# Changelog

## 1.2.0
 - Support Slick 2.1.0.

## 1.1.0
 - Added DateTimeZone support.

## 1.0.1
 - Added JdbcJodaSupport.
