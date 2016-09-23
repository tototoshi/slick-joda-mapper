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

import slick.driver._

class GenericJodaSupport(val driver: JdbcProfile) extends BaseGenericJodaSupport {
  def profile = driver
}

trait BaseGenericJodaSupport {
  def profile: JdbcProfile

  protected val dateTimeZoneMapperDelegate = new JodaDateTimeZoneMapper(profile)
  protected val localDateMapperDelegate = new JodaLocalDateMapper(profile)
  protected val dateTimeMapperDelegate = new JodaDateTimeMapper(profile)
  protected val instantMapperDelegate = new JodaInstantMapper(profile)
  protected val localDateTimeMapperDelegate = new JodaLocalDateTimeMapper(profile)
  protected val localTimeMapperDelegate = new JodaLocalTimeMapper(profile)

  implicit val dateTimeZoneTypeMapper = dateTimeZoneMapperDelegate.TypeMapper
  implicit val getDateTimeZoneResult = dateTimeZoneMapperDelegate.JodaGetResult.getResult
  implicit val getDateTimeZoneOptionResult = dateTimeZoneMapperDelegate.JodaGetResult.getOptionResult
  implicit val setDateTimeZoneParameter = dateTimeZoneMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setDateTimeZoneOptionParameter = dateTimeZoneMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val localDateTypeMapper = localDateMapperDelegate.TypeMapper
  implicit val getLocalDateResult = localDateMapperDelegate.JodaGetResult.getResult
  implicit val getLocalDateOptionResult = localDateMapperDelegate.JodaGetResult.getOptionResult
  implicit val setLocalDateParameter = localDateMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setLocalDateOptionParameter = localDateMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val datetimeTypeMapper = dateTimeMapperDelegate.TypeMapper
  implicit val getDatetimeResult = dateTimeMapperDelegate.JodaGetResult.getResult
  implicit val getDatetimeOptionResult = dateTimeMapperDelegate.JodaGetResult.getOptionResult
  implicit val setDatetimeParameter = dateTimeMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setDatetimeOptionParameter = dateTimeMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val instantTypeMapper = instantMapperDelegate.TypeMapper
  implicit val getInstantResult = instantMapperDelegate.JodaGetResult.getResult
  implicit val getInstantOptionResult = instantMapperDelegate.JodaGetResult.getOptionResult
  implicit val setInstantParameter = instantMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setInstantOptionParameter = instantMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val localDatetimeTypeMapper = localDateTimeMapperDelegate.TypeMapper
  implicit val getLocalDatetimeResult = localDateTimeMapperDelegate.JodaGetResult.getResult
  implicit val getLocalDatetimeOptionResult = localDateTimeMapperDelegate.JodaGetResult.getOptionResult
  implicit val setLocalDatetimeParameter = localDateTimeMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setLocalDatetimeOptionParameter = localDateTimeMapperDelegate.JodaSetParameter.setJodaOptionParameter

  implicit val localTimeTypeMapper = localTimeMapperDelegate.TypeMapper
  implicit val getLocalTimeResult = localTimeMapperDelegate.JodaGetResult.getResult
  implicit val getLocalTimeOptionResult = localTimeMapperDelegate.JodaGetResult.getOptionResult
  implicit val setLocalTimeParameter = localTimeMapperDelegate.JodaSetParameter.setJodaParameter
  implicit val setLocalTimeOptionParameter = localTimeMapperDelegate.JodaSetParameter.setJodaOptionParameter

}

object H2JodaSupport extends GenericJodaSupport(H2Driver)
object PostgresJodaSupport extends GenericJodaSupport(PostgresDriver)
object MySQLJodaSupport extends GenericJodaSupport(MySQLDriver)
object HsqldbJodaSupport extends GenericJodaSupport(HsqldbDriver)
object SQLiteJodaSupport extends GenericJodaSupport(SQLiteDriver)

trait H2JodaSupport extends BaseGenericJodaSupport { def profile = H2Driver }
trait PostgresJodaSupport extends BaseGenericJodaSupport { def profile = PostgresDriver }
trait MySQLJodaSupport extends BaseGenericJodaSupport { def profile = MySQLDriver }
trait HsqldbJodaSupport extends BaseGenericJodaSupport { def profile = HsqldbDriver }
trait SQLiteJodaSupport extends BaseGenericJodaSupport { def profile = SQLiteDriver }
