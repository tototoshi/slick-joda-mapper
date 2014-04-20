/*
 * Copyright 2013 Toshiyuki Takahashi
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.tototoshi.slick

import scala.slick.driver.JdbcDriver
import org.joda.time._
import com.github.tototoshi.slick.converter._
import java.sql.{ Time, Timestamp, Date }
import scala.slick.jdbc.{ PositionedResult, PositionedParameters }

class JodaDateTimeZoneMapper(val driver: JdbcDriver) {

  object TypeMapper extends driver.DriverJdbcType[DateTimeZone]
      with JodaDateTimeZoneSqlStringConverter {
    def sqlType = java.sql.Types.VARCHAR
    def setValue(v: DateTimeZone, p: PositionedParameters) =
      p.setString(toSqlType(v))
    def setOption(v: Option[DateTimeZone], p: PositionedParameters) =
      p.setStringOption(v.map(toSqlType))
    def nextValue(r: PositionedResult) = {
      fromSqlType(r.nextString())
    }
    def updateValue(v: DateTimeZone, r: PositionedResult) = r.updateString(toSqlType(v))
    override def valueToSQLLiteral(value: DateTimeZone) = toSqlType(value)
  }

  object JodaGetResult extends JodaGetResult[String, DateTimeZone] with JodaDateTimeZoneSqlStringConverter {
    def next(rs: PositionedResult): String = rs.nextString()
    def nextOption(rs: PositionedResult): Option[String] = rs.nextStringOption()
  }

  object JodaSetParameter extends JodaSetParameter[String, DateTimeZone] with JodaDateTimeZoneSqlStringConverter {
    def set(rs: PositionedParameters, z: String): Unit = rs.setString(z)
    def setOption(rs: PositionedParameters, z: Option[String]): Unit = rs.setStringOption(z)
  }

}

class JodaLocalDateMapper(val driver: JdbcDriver) {

  object TypeMapper extends driver.DriverJdbcType[LocalDate]
      with JodaLocalDateSqlDateConverter {
    def zero = new LocalDate(0L)
    def sqlType = java.sql.Types.DATE
    def setValue(v: LocalDate, p: PositionedParameters) =
      p.setDate(toSqlType(v))
    def setOption(v: Option[LocalDate], p: PositionedParameters) =
      p.setDateOption(v.map(toSqlType))
    def nextValue(r: PositionedResult) = {
      fromSqlType(r.nextDate())
    }
    def updateValue(v: LocalDate, r: PositionedResult) = r.updateDate(toSqlType(v))
    override def valueToSQLLiteral(value: LocalDate) = "{d '" + toSqlType(value).toString + "'}"
  }

  object JodaGetResult extends JodaGetResult[java.sql.Date, LocalDate] with JodaLocalDateSqlDateConverter {
    def next(rs: PositionedResult): Date = rs.nextDate()
    def nextOption(rs: PositionedResult): Option[Date] = rs.nextDateOption()
  }

  object JodaSetParameter extends JodaSetParameter[java.sql.Date, LocalDate] with JodaLocalDateSqlDateConverter {
    def set(rs: PositionedParameters, d: Date): Unit = rs.setDate(d)
    def setOption(rs: PositionedParameters, d: Option[Date]): Unit = rs.setDateOption(d)
  }

}

class JodaDateTimeMapper(val driver: JdbcDriver) {

  object TypeMapper extends driver.DriverJdbcType[DateTime]
      with JodaDateTimeSqlTimestampConverter {
    def zero = new DateTime(0L)
    def sqlType = java.sql.Types.TIMESTAMP
    def setValue(v: DateTime, p: PositionedParameters) =
      p.setTimestamp(toSqlType(v))
    def setOption(v: Option[DateTime], p: PositionedParameters) =
      p.setTimestampOption(v.map(toSqlType))
    def nextValue(r: PositionedResult) = {
      fromSqlType(r.nextTimestamp())
    }
    def updateValue(v: DateTime, r: PositionedResult) = r.updateTimestamp(toSqlType(v))
    override def valueToSQLLiteral(value: DateTime) = "{ts '" + toSqlType(value).toString + "'}"
  }

  object JodaGetResult extends JodaGetResult[Timestamp, DateTime] with JodaDateTimeSqlTimestampConverter {
    def next(rs: PositionedResult): Timestamp = rs.nextTimestamp()
    def nextOption(rs: PositionedResult): Option[Timestamp] = rs.nextTimestampOption()
  }

  object JodaSetParameter extends JodaSetParameter[Timestamp, DateTime] with JodaDateTimeSqlTimestampConverter {
    def set(rs: PositionedParameters, d: Timestamp): Unit = rs.setTimestamp(d)
    def setOption(rs: PositionedParameters, d: Option[Timestamp]): Unit = rs.setTimestampOption(d)
  }

}

class JodaInstantMapper(val driver: JdbcDriver) {

  object TypeMapper extends driver.DriverJdbcType[Instant]
      with JodaInstantSqlTimestampConverter {
    def zero = new DateTime(0L)
    def sqlType = java.sql.Types.TIMESTAMP
    def setValue(v: Instant, p: PositionedParameters) =
      p.setTimestamp(toSqlType(v))
    def setOption(v: Option[Instant], p: PositionedParameters) =
      p.setTimestampOption(v.map(toSqlType))
    def nextValue(r: PositionedResult) = {
      fromSqlType(r.nextTimestamp())
    }
    def updateValue(v: Instant, r: PositionedResult) = r.updateTimestamp(toSqlType(v))
    override def valueToSQLLiteral(value: Instant) = "{ts '" + toSqlType(value).toString + "'}"
  }

  object JodaGetResult extends JodaGetResult[Timestamp, Instant] with JodaInstantSqlTimestampConverter {
    def next(rs: PositionedResult): Timestamp = rs.nextTimestamp()
    def nextOption(rs: PositionedResult): Option[Timestamp] = rs.nextTimestampOption()
  }

  object JodaSetParameter extends JodaSetParameter[Timestamp, Instant] with JodaInstantSqlTimestampConverter {
    def set(rs: PositionedParameters, d: Timestamp): Unit = rs.setTimestamp(d)
    def setOption(rs: PositionedParameters, d: Option[Timestamp]): Unit = rs.setTimestampOption(d)
  }

}

class JodaLocalDateTimeMapper(val driver: JdbcDriver) {

  object TypeMapper extends driver.DriverJdbcType[LocalDateTime]
      with JodaLocalDateTimeSqlTimestampConverter {
    def zero = new LocalDateTime(0L)
    def sqlType = java.sql.Types.TIMESTAMP
    def setValue(v: LocalDateTime, p: PositionedParameters) =
      p.setTimestamp(toSqlType(v))
    def setOption(v: Option[LocalDateTime], p: PositionedParameters) =
      p.setTimestampOption(v.map(toSqlType))
    def nextValue(r: PositionedResult) = {
      fromSqlType(r.nextTimestamp())
    }
    def updateValue(v: LocalDateTime, r: PositionedResult) = r.updateTimestamp(toSqlType(v))
    override def valueToSQLLiteral(value: LocalDateTime) = "{ts '" + toSqlType(value).toString + "'}"
  }

  object JodaGetResult extends JodaGetResult[Timestamp, LocalDateTime] with JodaLocalDateTimeSqlTimestampConverter {
    def next(rs: PositionedResult): Timestamp = rs.nextTimestamp()
    def nextOption(rs: PositionedResult): Option[Timestamp] = rs.nextTimestampOption()
  }

  object JodaSetParameter extends JodaSetParameter[Timestamp, LocalDateTime] with JodaLocalDateTimeSqlTimestampConverter {
    def set(rs: PositionedParameters, d: Timestamp): Unit = rs.setTimestamp(d)
    def setOption(rs: PositionedParameters, d: Option[Timestamp]): Unit = rs.setTimestampOption(d)
  }

}

class JodaLocalTimeMapper(val driver: JdbcDriver) {

  object TypeMapper extends driver.DriverJdbcType[LocalTime]
      with JodaLocalTimeSqlTimeConverter {
    def zero = new LocalTime(0L)
    def sqlType = java.sql.Types.TIME
    def setValue(v: LocalTime, p: PositionedParameters) =
      p.setTime(toSqlType(v))
    def setOption(v: Option[LocalTime], p: PositionedParameters) =
      p.setTimeOption(v.map(toSqlType))
    def nextValue(r: PositionedResult) = {
      fromSqlType(r.nextTime())
    }
    def updateValue(v: LocalTime, r: PositionedResult) = r.updateTime(toSqlType(v))
    override def valueToSQLLiteral(value: LocalTime) = "{t '" + toSqlType(value).toString + "'}"
  }

  object JodaGetResult extends JodaGetResult[Time, LocalTime] with JodaLocalTimeSqlTimeConverter {
    def next(rs: PositionedResult): Time = rs.nextTime()
    def nextOption(rs: PositionedResult): Option[Time] = rs.nextTimeOption()
  }

  object JodaSetParameter extends JodaSetParameter[Time, LocalTime] with JodaLocalTimeSqlTimeConverter {
    def set(rs: PositionedParameters, d: Time): Unit = rs.setTime(d)
    def setOption(rs: PositionedParameters, d: Option[Time]): Unit = rs.setTimeOption(d)
  }

}

