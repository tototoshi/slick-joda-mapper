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

import org.scalatest.{ BeforeAndAfter, FunSpec }
import org.scalatest.matchers._
import scala.slick.driver.H2Driver.simple._
import com.github.tototoshi.slick.H2JodaSupport._
import org.joda.time._
import scala.slick.jdbc.GetResult
import scala.slick.jdbc.StaticQuery.interpolation
import java.util.{ TimeZone, Locale }
import scala.slick.lifted.{ TableQuery, Tag }

case class Jodas(localDate: LocalDate,
  dateTime: DateTime,
  localDateTime: LocalDateTime,
  localTime: LocalTime,
  optLocalDate: Option[LocalDate],
  optDateTime: Option[DateTime],
  optLocalDateTime: Option[LocalDateTime],
  optLocalTime: Option[LocalTime])

class JodaTest(tag: Tag) extends Table[Jodas](tag, "JODA_TEST") {
  def localDate = column[LocalDate]("LOCAL_DATE")
  def dateTime = column[DateTime]("DATE_TIME")
  def localDateTime = column[LocalDateTime]("LOCAL_DATE_TIME")
  def localTime = column[LocalTime]("LOCAL_TIME")
  def optLocalDate = column[Option[LocalDate]]("OPT_LOCAL_DATE")
  def optDateTime = column[Option[DateTime]]("OPT_DATE_TIME")
  def optLocalDateTime = column[Option[LocalDateTime]]("OPT_LOCAL_DATE_TIME")
  def optLocalTime = column[Option[LocalTime]]("OPT_LOCAL_TIME")
  def * = (localDate, dateTime, localDateTime, localTime, optLocalDate, optDateTime, optLocalDateTime, optLocalTime) <> (Jodas.tupled, Jodas.unapply _)
}

class JodaSupportSpec extends FunSpec
    with ShouldMatchers
    with BeforeAndAfter {

  val db = Database.forURL("jdbc:h2:memory:test",
    driver = "org.h2.Driver",
    user = "sa",
    password = null)

  val jodaTest = TableQuery[JodaTest]

  before {
    Locale.setDefault(Locale.JAPAN)
    val tz = TimeZone.getTimeZone("Asia/Tokyo")
    TimeZone.setDefault(tz)
    DateTimeZone.setDefault(DateTimeZone.forID(tz.getID))

    db withSession { implicit session: Session =>
      jodaTest.ddl.create
    }
  }

  after {
    db withSession { implicit session: Session =>
      jodaTest.ddl.drop
    }
  }

  def insertTestData()(implicit session: Session): Unit = {
    jodaTest.insert(
      Jodas(
        new LocalDate(2012, 12, 4),
        new DateTime(2012, 12, 4, 0, 0, 0, 0),
        new LocalDateTime(2012, 12, 4, 0, 0, 0, 0),
        new LocalTime(0),
        Some(new LocalDate(2012, 12, 4)),
        Some(new DateTime(2012, 12, 4, 0, 0, 0, 0)),
        Some(new LocalDateTime(2012, 12, 4, 0, 0, 0, 0)),
        Some(new LocalTime(0))
      )
    )
    jodaTest.insert(
      Jodas(
        new LocalDate(2012, 12, 5),
        new DateTime(2012, 12, 5, 0, 0, 0, 0),
        new LocalDateTime(2012, 12, 5, 0, 0, 0, 0),
        new LocalTime(0),
        Some(new LocalDate(2012, 12, 5)),
        None,
        None,
        Some(new LocalTime(0))

      )
    )
    jodaTest.insert(
      Jodas(
        new LocalDate(2012, 12, 6),
        new DateTime(2012, 12, 6, 0, 0, 0, 0),
        new LocalDateTime(2012, 12, 6, 0, 0, 0, 0),
        new LocalTime(0),
        Some(new LocalDate(2012, 12, 6)),
        None,
        None,
        Some(new LocalTime(0))
      )
    )
  }

  describe("JodaSupport") {

    it("should enable us to use joda-time with slick") {
      db withSession { implicit session: Session =>
        insertTestData()
        val record = (for { j <- jodaTest } yield j).list()
        record should have size (3)
      }
    }

    it("should enable us to use joda-time with string interpolation API") {
      db withSession { implicit session: Session =>
        insertTestData()
        sql"SELECT opt_local_date FROM joda_test WHERE local_date = ${new LocalDate(2012, 12, 4)}"
          .as[Option[LocalDate]].first should be(Some(new LocalDate(2012, 12, 4)))
        sql"SELECT opt_date_time FROM joda_test WHERE date_time = ${new DateTime(2012, 12, 4, 0, 0, 0, 0)}"
          .as[Option[DateTime]].first should be(Some(new DateTime(2012, 12, 4, 0, 0, 0, 0)))
        sql"SELECT opt_local_date_time FROM joda_test WHERE local_date_time = ${new LocalDateTime(2012, 12, 4, 0, 0, 0, 0)}"
          .as[Option[LocalDateTime]].first should be(Some(new LocalDateTime(2012, 12, 4, 0, 0, 0, 0)))
        sql"SELECT opt_local_time FROM joda_test WHERE local_time = ${new LocalTime(0)}"
          .as[Option[LocalTime]].first should be(Some(new LocalTime(0)))
        sql"SELECT local_date FROM joda_test WHERE opt_local_date = ${Some(new LocalDate(2012, 12, 5))}"
          .as[LocalDate].first should be(new LocalDate(2012, 12, 5))
        sql"SELECT date_time FROM joda_test WHERE opt_date_time = ${Some(new DateTime(2012, 12, 4, 0, 0, 0, 0))}"
          .as[DateTime].first should be(new DateTime(2012, 12, 4, 0, 0, 0, 0))
        sql"SELECT local_date_time FROM joda_test WHERE opt_local_date_time = ${Some(new LocalDateTime(2012, 12, 4, 0, 0, 0, 0))}"
          .as[LocalDateTime].first should be(new LocalDateTime(2012, 12, 4, 0, 0, 0, 0))
        sql"SELECT local_time FROM joda_test WHERE opt_local_time = ${Some(new LocalTime(0))}"
          .as[LocalTime].first should be(new LocalTime(0))

        implicit val getResult: GetResult[(LocalDate, DateTime, LocalDateTime, LocalTime)] = GetResult(r => (r.<<, r.<<, r.<<, r.<<))
        implicit val getResult2: GetResult[Jodas] = GetResult(r => Jodas(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

        sql"SELECT local_date, date_time, local_date_time, local_time FROM joda_test".as[(LocalDate, DateTime, LocalDateTime, LocalTime)].list() should have size (3)
        sql"SELECT local_date, date_time, local_date_time, local_time, opt_local_date, opt_date_time, opt_local_date_time, opt_local_time FROM joda_test".as[Jodas].list() should have size (3)
      }
    }

    it("can be used with comparative operators") {
      db withSession { implicit session: Session =>
        insertTestData()

        val q1 = for {
          jt <- jodaTest
          if jt.localDate > new LocalDate(2012, 12, 5)
        } yield jt

        q1.list should have size (1)
      }
    }

    it("should be able to filter with the specified date") {
      db withSession { implicit session: Session =>
        insertTestData()

        val q1 = for {
          jt <- jodaTest
          if jt.localDate === new LocalDate(2012, 12, 5)
        } yield jt

        val res1 = q1.list
        res1 should have size (1)
        res1.headOption.map(_.localDate) should be(Some(new LocalDate(2012, 12, 5)))

        val q2 = for {
          jt <- jodaTest
          if jt.localDate =!= new LocalDate(2012, 12, 5)
        } yield jt
        val res2 = q2.list
        res2 should have size (2)
        res2.lift(1).map(_.localDate) should not be (Some(new LocalDate(2012, 12, 5)))
        res2.lift(2).map(_.localDate) should not be (Some(new LocalDate(2012, 12, 5)))
      }
    }

  }
}

