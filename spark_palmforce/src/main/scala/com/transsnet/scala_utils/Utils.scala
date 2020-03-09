package com.transsnet.scala_utils

/**
 * User: Jansion
 * CreatedTime: 2018/9/10 22:22
 * Date: 2018/9/10
 * Time: 22:22
 * Description: 
 */

import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import java.util.{Base64, Calendar, Date, Locale}

import scala.math.BigDecimal.RoundingMode


object Utils {
  //四舍五入
  def getScale(doubleValue: Double, scale: Int) = {
    val bigDecimal = new BigDecimal(doubleValue)
    bigDecimal.setScale(scale, RoundingMode.HALF_UP)
  }

  //日志的加减
  def caculateDate(longTime: Long, day: Int) = {
    val calendar = Calendar.getInstance()
    calendar.setTimeInMillis(longTime)
    calendar.add(Calendar.DAY_OF_MONTH, day)
    calendar.getTimeInMillis
  }

  //验证是否是一个数字
  def validateNumber(numberString: String) = {
    val reg = "[0-9]{1,}"
    val pattern = Pattern.compile(reg)
    pattern.matcher(numberString).matches()
  }

  //验证ip
  def validate(ip: String) = {
    val reg = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))"
    val pattern = Pattern.compile(reg)
    pattern.matcher(ip).matches()
  }

  //将日志服务器时间转换成时间戳
  def parseLogServerTimeToLong(accessTime: String) = {
    val simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss +0800", Locale.ENGLISH)
    val date = simpleDateFormat.parse(accessTime)
    date.getTime
  }

  //base64解码
  def base64Decode(base64EncodeString: String) = {
    Base64.getDecoder.decode(base64EncodeString)
  }

  //验证日期是否yyyy-MM-dd
  def validateDate(date: String) = {
    val reg = "((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$"
    val pattern = Pattern.compile(reg)
    pattern.matcher(date).matches()
  }

  /**
   * 将时间戳转换成指定格式的日期
   *
   * @param longTime
   * @param pattern
   * @return
   */
  def formatDate(longTime: Long, pattern: String) = {
    val sdf = new SimpleDateFormat(pattern)
    val calendar = sdf.getCalendar
    calendar.setTimeInMillis(longTime)
    sdf.format(calendar.getTime)
  }

  /**
   * 将字符串时间转换成 long类型的时间戳
   *
   * @param strDate 2017-10-23
   * @param pattern yyyy-MM-dd
   */
  def parseDateToLong(strDate: String, pattern: String) = {
    val sdf = new SimpleDateFormat(pattern)
    sdf.parse(strDate).getTime
  }

  /**
   * 格式化时间
   * @param date
   * @param pattern
   * @return
   */
  def formateDateToDate(date: Date, pattern: String) = {
    val sdf = new SimpleDateFormat(pattern)
    sdf.format(date)

  }

  def thisMonthFirstDay() ={
    val calendar = Calendar.getInstance()
     calendar.set(Calendar.DAY_OF_MONTH,1)
     calendar.getTime
  }

  /**
   * 返回是否是第一个月的首日
   * @param date
   * @return
   */
  def  isThisMonthFirstDay(date:Date) = {
    formateDateToDate(date,"dd") == "01"
  }



}
