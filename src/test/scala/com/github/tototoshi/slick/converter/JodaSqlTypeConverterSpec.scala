package com.github.tototoshi.slick.converter

import org.joda.time.{ DateTimeZone, DateTime, LocalDate, LocalTime }
import java.sql.Time
import org.scalatest.{ BeforeAndAfter, FunSpec }
import org.scalatest.matchers._
import java.util.{ TimeZone, Locale }

class JodaSqlTypeConverterSpec extends FunSpec with ShouldMatchers with BeforeAndAfter {

  def fixture = new {
    val localDateConverter = new JodaLocalDateSqlDateConverter {}
    val dateTimeConverter = new JodaDateTimeSqlTimestampConverter {}
    val localTimeConverter = new JodaLocalTimeSqlTimeConverter {}
  }

  before {
    Locale.setDefault(Locale.JAPAN)
    val tz = TimeZone.getTimeZone("Asia/Tokyo")
    TimeZone.setDefault(tz)
    DateTimeZone.setDefault(DateTimeZone.forID(tz.getID))
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
      fixture.dateTimeConverter.toSqlType(null) should be(null)
      val current = DateTime.now
      val timestamp = current.getMillis
      fixture.dateTimeConverter.toSqlType(current).getTime should be(timestamp)
    }

    it("should convert java.sql.Timestamp to DateTime") {
      fixture.dateTimeConverter.fromSqlType(null) should be(null)
      val current = DateTime.now
      val timestamp = current.getMillis
      fixture.dateTimeConverter.fromSqlType(new java.sql.Timestamp(timestamp)) should be(current)
    }

  }

  describe("JodaLocalTimeSqlTimeConverter") {

    val offset = {
      import java.util.Calendar
      val cal = Calendar.getInstance
      cal.get(Calendar.ZONE_OFFSET)
    }

    it("should convert LocalTime to java.sql.Time") {
      fixture.localTimeConverter.fromSqlType(null) should be(null)
      val current = LocalTime.now
      fixture.localTimeConverter.toSqlType(current) should be(new Time(current.toDateTimeToday.getMillis))
    }

    it("should convert java.sql.Time to LocalTime") {
      fixture.localTimeConverter.fromSqlType(null) should be(null)
      val current = LocalTime.now
      val timestamp = current.toDateTimeToday.getMillis
      fixture.localTimeConverter.fromSqlType(new java.sql.Time(timestamp)) should be(current)
    }

  }

}

