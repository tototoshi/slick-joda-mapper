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

import scala.slick.driver._
import scala.slick.lifted.BaseTypeMapper
import scala.slick.session.{ PositionedParameters, PositionedResult }
import com.github.tototoshi.slick.converter._
import org.joda.time.{ LocalDate, DateTime, LocalTime }
import scala.slick.jdbc.{ SetParameter, GetResult }

trait JodaLocalDateMapper { driver: ExtendedDriver =>

  object localDateTypeMapper extends BaseTypeMapper[LocalDate] {

    def apply(profile: BasicProfile) = localDateTypeMapperDelegate

    val localDateTypeMapperDelegate = new LocalDateJdbcType

    class LocalDateJdbcType extends DriverTypeMapperDelegate[LocalDate]
        with JodaLocalDateSqlDateConverter {
      def zero = new LocalDate(0L)
      def sqlType = java.sql.Types.DATE
      def setValue(v: LocalDate, p: PositionedParameters) =
        p.setDate(toSqlType(v))
      def setOption(v: Option[LocalDate], p: PositionedParameters) =
        p.setDateOption(v.map(toSqlType))
      def nextValue(r: PositionedResult) = {
        fromSqlType(r.nextDate)
      }
      def updateValue(v: LocalDate, r: PositionedResult) = r.updateDate(toSqlType(v))
      override def valueToSQLLiteral(value: LocalDate) = "{d '" + toSqlType(value).toString + "'}"
    }

  }

  object getLocalDateResult extends GetResult[LocalDate]
      with JodaLocalDateSqlDateConverter {
    def apply(rs: PositionedResult) = fromSqlType(rs.nextDate())
  }

  object getLocalDateOptionResult extends GetResult[Option[LocalDate]]
      with JodaLocalDateSqlDateConverter {
    def apply(rs: PositionedResult) = rs.nextDateOption().map(fromSqlType)
  }

  object setLocalDateParameter extends SetParameter[LocalDate]
      with JodaLocalDateSqlDateConverter {
    def apply(d: LocalDate, p: PositionedParameters) {
      p.setDate(toSqlType(d))
    }
  }

  object setLocalDateOptionParameter extends SetParameter[Option[LocalDate]]
      with JodaLocalDateSqlDateConverter {
    def apply(d: Option[LocalDate], p: PositionedParameters) {
      p.setDateOption(d.map(toSqlType))
    }
  }

}

trait JodaDateTimeMapper { driver: ExtendedDriver =>

  object dateTimeTypeMapper extends BaseTypeMapper[DateTime] {

    def apply(profile: BasicProfile) = dateTimeTypeMapperDelegate

    val dateTimeTypeMapperDelegate = new DateTimeJdbcType

    class DateTimeJdbcType extends DriverTypeMapperDelegate[DateTime]
        with JodaDateTimeSqlTimestampConverter {
      def zero = new DateTime(0L)
      def sqlType = java.sql.Types.TIMESTAMP
      def setValue(v: DateTime, p: PositionedParameters) =
        p.setTimestamp(toSqlType(v))
      def setOption(v: Option[DateTime], p: PositionedParameters) =
        p.setTimestampOption(v.map(toSqlType))
      def nextValue(r: PositionedResult) = {
        fromSqlType(r.nextTimestamp)
      }
      def updateValue(v: DateTime, r: PositionedResult) = r.updateTimestamp(toSqlType(v))
      override def valueToSQLLiteral(value: DateTime) = "{ts '" + toSqlType(value).toString + "'}"
    }

  }

  object getDateTimeResult extends GetResult[DateTime]
      with JodaDateTimeSqlTimestampConverter {
    def apply(rs: PositionedResult) = fromSqlType(rs.nextTimestamp())
  }

  object getDateTimeOptionResult extends GetResult[Option[DateTime]]
      with JodaDateTimeSqlTimestampConverter {
    def apply(rs: PositionedResult) = rs.nextTimestampOption().map(fromSqlType)
  }

  object setDateTimeParameter extends SetParameter[DateTime]
      with JodaDateTimeSqlTimestampConverter {
    def apply(d: DateTime, p: PositionedParameters) {
      p.setTimestamp(toSqlType(d))
    }
  }

  object setDateTimeOptionParameter extends SetParameter[Option[DateTime]]
      with JodaDateTimeSqlTimestampConverter {
    def apply(d: Option[DateTime], p: PositionedParameters) {
      p.setTimestampOption(d.map(toSqlType))
    }
  }

}

trait JodaLocalTimeMapper { driver: ExtendedDriver =>

  object localTimeTypeMapper extends BaseTypeMapper[LocalTime] {

    def apply(profile: BasicProfile) = localTimeTypeMapperDelegate

    val localTimeTypeMapperDelegate = new LocalTimeJdbcType

    class LocalTimeJdbcType extends DriverTypeMapperDelegate[LocalTime]
        with JodaLocalTimeSqlTimeConverter {
      def zero = new LocalTime(0L)
      def sqlType = java.sql.Types.TIME
      def setValue(v: LocalTime, p: PositionedParameters) =
        p.setTime(toSqlType(v))
      def setOption(v: Option[LocalTime], p: PositionedParameters) =
        p.setTimeOption(v.map(toSqlType))
      def nextValue(r: PositionedResult) = {
        fromSqlType(r.nextTime)
      }
      def updateValue(v: LocalTime, r: PositionedResult) = r.updateTime(toSqlType(v))
      override def valueToSQLLiteral(value: LocalTime) = "{t '" + toSqlType(value).toString + "'}"
    }

  }

  object getLocalTimeResult extends GetResult[LocalTime]
      with JodaLocalTimeSqlTimeConverter {
    def apply(rs: PositionedResult) = fromSqlType(rs.nextTime())
  }

  object getLocalTimeOptionResult extends GetResult[Option[LocalTime]]
      with JodaLocalTimeSqlTimeConverter {
    def apply(rs: PositionedResult) = rs.nextTimeOption().map(fromSqlType)
  }

  object setLocalTimeParameter extends SetParameter[LocalTime]
      with JodaLocalTimeSqlTimeConverter {
    def apply(d: LocalTime, p: PositionedParameters) {
      p.setTime(toSqlType(d))
    }
  }

  object setLocalTimeOptionParameter extends SetParameter[Option[LocalTime]]
      with JodaLocalTimeSqlTimeConverter {
    def apply(d: Option[LocalTime], p: PositionedParameters) {
      p.setTimeOption(d.map(toSqlType))
    }
  }

}

object JodaMappers extends ExtendedDriver
  with JodaLocalDateMapper
  with JodaDateTimeMapper
  with JodaLocalTimeMapper

object JodaSupport {

  implicit val localDateTypeMapper = JodaMappers.localDateTypeMapper

  implicit val getLocalDateResult = JodaMappers.getLocalDateResult

  implicit val getLocalDateOptionResult = JodaMappers.getLocalDateOptionResult

  implicit val setLocalDateParameter = JodaMappers.setLocalDateParameter

  implicit val setLocalDateOptionParameter = JodaMappers.setLocalDateOptionParameter

  implicit val dateTimeTypeMapper = JodaMappers.dateTimeTypeMapper

  implicit val getDateTimeResult = JodaMappers.getDateTimeResult

  implicit val getDateTimeOptionResult = JodaMappers.getDateTimeOptionResult

  implicit val setDateTimeParameter = JodaMappers.setDateTimeParameter

  implicit val setDateTimeOptionParameter = JodaMappers.setDateTimeOptionParameter

  implicit val localTimeTypeMapper = JodaMappers.localTimeTypeMapper

  implicit val getLocalTimeResult = JodaMappers.getLocalTimeResult

  implicit val getLocalTimeOptionResult = JodaMappers.getLocalTimeOptionResult

  implicit val setLocalTimeParameter = JodaMappers.setLocalTimeParameter

  implicit val setLocalTimeOptionParameter = JodaMappers.setLocalTimeOptionParameter

}
