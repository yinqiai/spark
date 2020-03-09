package com.transsnet.implicit_01

//隐式参数-考试签到例子
class SignPen {
  def write(content: String) = println(content)
}

object Implicit03{
  //这个一定要声明为隐式
  implicit val signPen = new SignPen

  /**
    * 所谓的隐式参数，指的是在函数或者方法中，
    * 定义一个用implicit修饰的参数，此时Scala会尝试找到一个指定类型的，
    * 用implicit修饰的对象，即隐式值，并注入参数
    */
  def signForExam(name: String) (implicit signPen: SignPen) {
    signPen.write(name + " come to exam in time.")
  }

  def main (args: Array[String] ): Unit = {
    //这里因为是隐式参入参数implicit val signPen  所有后面一个参数括号可以省去
    signForExam("ll")
  }}

