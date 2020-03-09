package com.transsnet.implicit_01

//隐式参数-考试签到例子


object Implicit03_1{
  implicit val signPen = new SignPen
  implicit def studentIDto(a:Int):String = "笑趴下的孩子"


  def signForExam(id: Int) (implicit signPen: SignPen) {
    /**
      * 第二次用到隐式转换：scala 先看到id为int ,但是方法里面参数是String
      * 这时候会调用studentIDto把int转化为String
      */
    signPen.write(id)
  }

  def main (args: Array[String] ): Unit = {

    /**
      * 第一次用到隐式转换:scala会自动传入implicit val signPen
      * 所有后面一个参数括号可以省去
      */
    signForExam(11)
  }}

