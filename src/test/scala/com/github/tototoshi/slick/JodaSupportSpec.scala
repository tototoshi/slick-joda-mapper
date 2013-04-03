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

import org.scalatest.FunSpec
import org.scalatest.matchers._

class JodaSupportSpec extends FunSpec with ShouldMatchers {

  describe("JodaSupport") {

    it("should enable us to use joda-time with slick") {

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

      val db = Database.forURL("jdbc:h2:memory:test",
        driver = "org.h2.Driver",
        user = "sa",
        password = null)

      db withSession { implicit session: Session =>

        JodaTest.ddl.create

        JodaTest.insert(
          new LocalDate(2012, 12, 4),
          new DateTime(2012, 12, 4, 0, 0, 0, 0),
          new LocalTime(0),
          Some(new LocalDate(2012, 12, 5)),
          None,
          Some(new LocalTime(0))
        )

        val record = (for { j <- JodaTest } yield j).firstOption

        record should be(
          Some((new LocalDate(2012, 12, 4),
            new DateTime(2012, 12, 4, 0, 0, 0, 0),
            new LocalTime(0),
            Some(new LocalDate(2012, 12, 5)),
            None,
            Some(new LocalTime(0))
          ))
        )

        JodaTest.ddl.drop

      }

    }

  }
}

