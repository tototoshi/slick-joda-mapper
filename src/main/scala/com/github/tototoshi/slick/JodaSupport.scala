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

import org.joda.time.{ DateTime, DateTimeZone, Instant, LocalDate, LocalDateTime, LocalTime }
import java.util.Calendar
import org.joda.time.DateTime
import slick.jdbc._

/**
 * @param setTimeZone `Calendar` parameter for [[https://docs.oracle.com/javase/8/docs/api/java/sql/PreparedStatement.html#setTimestamp-int-java.sql.Timestamp-java.util.Calendar-]]
 */
class GenericJodaSupport(val driver: JdbcProfile, setTimeZone: DateTime => Option[Calendar]) {

  def this(driver: JdbcProfile) = {
    this(driver, GenericJodaSupport.defaultSetTimeZoneFunction)
  }

  protected val dateTimeZoneMapperDelegate: JodaDateTimeZoneMapper = new JodaDateTimeZoneMapper(driver)
  protected val localDateMapperDelegate: JodaLocalDateMapper = new JodaLocalDateMapper(driver)
  protected val dateTimeMapperDelegate: JodaDateTimeMapper = new JodaDateTimeMapper(driver, setTimeZone)
  protected val instantMapperDelegate: JodaInstantMapper = new JodaInstantMapper(driver)
  protected val localDateTimeMapperDelegate: JodaLocalDateTimeMapper = new JodaLocalDateTimeMapper(driver)
  protected val localTimeMapperDelegate: JodaLocalTimeMapper = new JodaLocalTimeMapper(driver)

  implicit val dateTimeZoneTypeMapper: JdbcProfile#DriverJdbcType[DateTimeZone] = dateTimeZoneMapperDelegate.TypeMapper
  implicit val getDateTimeZoneResult: GetResult[DateTimeZone] = dateTimeZoneMapperDelegate.JodaGetResult.getResult
  implicit val getDateTimeZoneOptionResult: GetResult[Option[DateTimeZone]] = dateTimeZoneMapperDelegate.JodaGetResult.getOptionResult
  implicit val setDateTimeZoneParameter: SetParameter[DateTimeZone] = dateTimeZoneMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setDateTimeZoneOptionParameter: SetParameter[Option[DateTimeZone]] = dateTimeZoneMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val localDateTypeMapper: JdbcProfile#DriverJdbcType[LocalDate] = localDateMapperDelegate.TypeMapper
  implicit val getLocalDateResult: GetResult[LocalDate] = localDateMapperDelegate.JodaGetResult.getResult
  implicit val getLocalDateOptionResult: GetResult[Option[LocalDate]] = localDateMapperDelegate.JodaGetResult.getOptionResult
  implicit val setLocalDateParameter: SetParameter[LocalDate] = localDateMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setLocalDateOptionParameter: SetParameter[Option[LocalDate]] = localDateMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val datetimeTypeMapper: JdbcProfile#DriverJdbcType[DateTime] = dateTimeMapperDelegate.TypeMapper
  implicit val getDatetimeResult: GetResult[DateTime] = dateTimeMapperDelegate.JodaGetResult.getResult
  implicit val getDatetimeOptionResult: GetResult[Option[DateTime]] = dateTimeMapperDelegate.JodaGetResult.getOptionResult
  implicit val setDatetimeParameter: SetParameter[DateTime] = dateTimeMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setDatetimeOptionParameter: SetParameter[Option[DateTime]] = dateTimeMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val instantTypeMapper: JdbcProfile#DriverJdbcType[Instant] = instantMapperDelegate.TypeMapper
  implicit val getInstantResult: GetResult[Instant] = instantMapperDelegate.JodaGetResult.getResult
  implicit val getInstantOptionResult: GetResult[Option[Instant]] = instantMapperDelegate.JodaGetResult.getOptionResult
  implicit val setInstantParameter: SetParameter[Instant] = instantMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setInstantOptionParameter: SetParameter[Option[Instant]] = instantMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val localDatetimeTypeMapper: JdbcProfile#DriverJdbcType[LocalDateTime] = localDateTimeMapperDelegate.TypeMapper
  implicit val getLocalDatetimeResult: GetResult[LocalDateTime] = localDateTimeMapperDelegate.JodaGetResult.getResult
  implicit val getLocalDatetimeOptionResult: GetResult[Option[LocalDateTime]] = localDateTimeMapperDelegate.JodaGetResult.getOptionResult
  implicit val setLocalDatetimeParameter: SetParameter[LocalDateTime] = localDateTimeMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setLocalDatetimeOptionParameter: SetParameter[Option[LocalDateTime]] = localDateTimeMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val localTimeTypeMapper: JdbcProfile#DriverJdbcType[LocalTime] = localTimeMapperDelegate.TypeMapper
  implicit val getLocalTimeResult: GetResult[LocalTime] = localTimeMapperDelegate.JodaGetResult.getResult
  implicit val getLocalTimeOptionResult: GetResult[Option[LocalTime]] = localTimeMapperDelegate.JodaGetResult.getOptionResult
  implicit val setLocalTimeParameter: SetParameter[LocalTime] = localTimeMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setLocalTimeOptionParameter: SetParameter[Option[LocalTime]] = localTimeMapperDelegate.JodaSetParameter.setJodaOptionParameter

}

object GenericJodaSupport {
  val defaultSetTimeZoneFunction: DateTime => Option[Calendar] = datetime =>
    Some(Calendar.getInstance(datetime.getZone.toTimeZone))
}

object H2JodaSupport extends GenericJodaSupport(H2Profile)
object PostgresJodaSupport extends GenericJodaSupport(PostgresProfile)
object MySQLJodaSupport extends GenericJodaSupport(MySQLProfile)
object HsqldbJodaSupport extends GenericJodaSupport(HsqldbProfile)
object SQLiteJodaSupport extends GenericJodaSupport(SQLiteProfile)
object OracleJodaSupport extends GenericJodaSupport(OracleProfile)
object SQLServerJodaSupport extends GenericJodaSupport(SQLServerProfile)
