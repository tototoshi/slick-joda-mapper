package com.github.tototoshi.slick.converter

import org.joda.time.{ DateTime, LocalDate }
import org.scalatest.FunSpec
import org.scalatest.matchers._

class JodaSqlTypeConverterSpec extends FunSpec with ShouldMatchers {

  def fixture = new {
    val localDateConverter = new JodaLocalDateSqlDateConverter {}
    val datetimeConverter = new JodaDateTimeSqlTimestampConverter {}
  }

  describe("JodaLocalDateSqlDateConverter") {

    it("should convert LocalDate to java.sql.Date") {
      fixture.localDateConverter.toSqlType(null) should be(null)
      val date = new LocalDate(2013, 3, 23)
      fixture.localDateConverter.toSqlType(date).getTime should be(1363964400000L)
    }

    it("should convert java.sql.Date to LocalDate") {
      fixture.localDateConverter.fromSqlType(null) should be(null)
      fixture.localDateConverter.fromSqlType(new java.sql.Date(1364000000000L)) should be(new LocalDate(2013, 3, 23))
    }

  }

  describe("JodaDateTimeSqlDateConverter") {

    it("should convert DateTime to java.sql.Timestamp") {
      fixture.datetimeConverter.toSqlType(null) should be(null)
      val current = DateTime.now
      val timestamp = current.getMillis
      fixture.datetimeConverter.toSqlType(current).getTime should be(timestamp)
    }

    it("should convert java.sql.Timestamp to DateTime") {
      fixture.datetimeConverter.fromSqlType(null) should be(null)
      val current = DateTime.now
      val timestamp = current.getMillis
      fixture.datetimeConverter.fromSqlType(new java.sql.Timestamp(timestamp)) should be(current)
    }

  }

}

