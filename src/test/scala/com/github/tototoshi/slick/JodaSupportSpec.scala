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

import org.scalatest._
import org.scalatest.matchers._
import com.github.tototoshi.slick.JodaSupport._
import scala.slick.driver.PostgresDriver.simple._
import org.joda.time.{ DateTime, LocalDate, LocalTime }

object JodaTest extends Table[(LocalDate, DateTime, LocalTime, Option[LocalDate], Option[DateTime], Option[LocalTime])]("joda_test") {
  def localDate = column[LocalDate]("local_date")
  def dateTime = column[DateTime]("date_time")
  def localTime = column[LocalTime]("local_time")
  def optLocalDate = column[Option[LocalDate]]("opt_local_date")
  def optDateTime = column[Option[DateTime]]("opt_date_time")
  def optLocalTime = column[Option[LocalTime]]("opt_local_time")
  def * = localDate ~ dateTime ~ localTime ~ optLocalDate ~ optDateTime ~ optLocalTime
}

class JodaSupportSpec extends FunSpec
    with ShouldMatchers
    with BeforeAndAfter {

  val db = Database.forURL("jdbc:h2:memory:test",
    driver = "org.h2.Driver",
    user = "sa",
    password = null)

  before {
    db withSession { implicit session: Session =>
      JodaTest.ddl.create
    }
  }

  after {
    db withSession { implicit session: Session =>
      JodaTest.ddl.drop
    }
  }

  def insertTestData()(implicit session: Session): Unit = {
    JodaTest.insert(
      new LocalDate(2012, 12, 4),
      new DateTime(2012, 12, 4, 0, 0, 0, 0),
      new LocalTime(0),
      Some(new LocalDate(2012, 12, 5)),
      None,
      Some(new LocalTime(0))
    )
    JodaTest.insert(
      new LocalDate(2012, 12, 5),
      new DateTime(2012, 12, 4, 0, 0, 0, 0),
      new LocalTime(0),
      Some(new LocalDate(2012, 12, 5)),
      None,
      Some(new LocalTime(0))
    )
    JodaTest.insert(
      new LocalDate(2012, 12, 6),
      new DateTime(2012, 12, 4, 0, 0, 0, 0),
      new LocalTime(0),
      Some(new LocalDate(2012, 12, 5)),
      None,
      Some(new LocalTime(0))
    )
  }

  describe("JodaSupport") {

    it("should enable us to use joda-time with slick") {
      db withSession { implicit session: Session =>
        insertTestData()
        val record = (for { j <- JodaTest } yield j).list()
        record should have size (3)
      }
    }

    it("can be used with comparative operators") {
      db withSession { implicit session: Session =>
        insertTestData()

        val q1 = for {
          jt <- JodaTest
          if jt.localDate > new LocalDate(2012, 12, 5)
        } yield jt

        q1.list should have size (1)
      }
    }

    it("should be able to filter with the specified date") {
      db withSession { implicit session: Session =>
        insertTestData()

        val q1 = for {
          jt <- JodaTest
          if jt.localDate === new LocalDate(2012, 12, 5)
        } yield jt

        val res1 = q1.list
        res1 should have size (1)
        res1.headOption.map(_._1) should be(Some(new LocalDate(2012, 12, 5)))

        val q2 = for {
          jt <- JodaTest
          if jt.localDate =!= new LocalDate(2012, 12, 5)
        } yield jt
        val res2 = q2.list
        res2 should have size (2)
        res2.lift(1).map(_._1) should not be (Some(new LocalDate(2012, 12, 5)))
        res2.lift(2).map(_._1) should not be (Some(new LocalDate(2012, 12, 5)))
      }
    }

  }
}

