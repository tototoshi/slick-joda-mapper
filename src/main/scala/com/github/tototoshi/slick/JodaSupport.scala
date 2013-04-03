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
    }

  }

}

object JodaMappers extends ExtendedDriver
  with JodaLocalDateMapper
  with JodaDateTimeMapper
  with JodaLocalTimeMapper

object JodaSupport {

  implicit val localDateTypeMapper = JodaMappers.localDateTypeMapper

  implicit val dateTimeTypeMapper = JodaMappers.dateTimeTypeMapper

  implicit val localTimeTypeMapper = JodaMappers.localTimeTypeMapper

}
