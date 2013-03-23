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
package com.github.tototoshi.slick.converter

import org.joda.time.{ LocalDate, DateTime }
import java.sql.Timestamp

trait JodaLocalDateSqlDateConverter
    extends SqlTypeConverter[java.sql.Date, LocalDate] {

  def toSqlType(d: LocalDate): java.sql.Date =
    if (d == null) null else millisToSqlType(d.toDate)

  def fromSqlType(d: java.sql.Date): LocalDate =
    if (d == null) null else new LocalDate(d.getTime)

}

trait JodaDateTimeSqlTimestampConverter
    extends SqlTypeConverter[Timestamp, DateTime] {

  def fromSqlType(t: java.sql.Timestamp): DateTime =
    if (t == null) null else new DateTime(t.getTime)

  def toSqlType(t: DateTime): java.sql.Timestamp =
    if (t == null) null else new java.sql.Timestamp(t.getMillis)

}
