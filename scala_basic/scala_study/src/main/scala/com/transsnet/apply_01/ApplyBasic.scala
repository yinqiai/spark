package com.transsnet.apply_01

class ApplyTest1{

  val name="clow";
  def apply()  {
    println("class ApplyTest--apply()...");
  }

}

//object下的成员默认都是静态的
object ApplyTest1{
  def apply() = {
    println("object ApplyTest--apply()...");
    new ApplyTest1()
  }
}


object ApplyBasic {

  def main(args: Array[String]) {
    //类名()->调用了对应object下的apply方法
    var a1=ApplyTest1()
    println(a1.name)
    //对象名()->调用了对应class的apply方法
    a1() //输出:class ApplyTest--apply()...
  }
}

