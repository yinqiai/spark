package com.transsnet.implicit_01

/**
  * 超人变身 --使用隐式转换加强现有类型（调用类的不存在的方法）
  * 隐式转换非常强大的一个功能，就是可以在不知不觉中加强现有类型的功能。也就是说，可以为某个类 定义一个加强版的类，并且定义互相之间的隐式转换，从而让源类在使用加强版的方式时，由Scala自动进行隐式转换为加强类，然后再调用该方法。
  * 当man这类调用 emitLaser时，会发现自己没有这个方法，就会在附近查找是否有用man类型作为参数的隐式函数，如果发现就进行匹配
  */
object Implicit02 {
  class Man(val name:String)
  class Superman(val name:String){
    def emitLaser = println("emit a laster")
  }

  implicit def man2superman(man: Man): Superman ={
    new Superman(man.name)
  }

  def main(args: Array[String]): Unit = {
    val leo = new Man("leo")
    //Scala自动调用隐式函数转化man 为superman 去调用superman 里面的emitLaser
    leo.emitLaser
  }
}
