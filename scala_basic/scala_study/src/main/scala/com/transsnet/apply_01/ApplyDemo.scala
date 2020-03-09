package com.transsnet.apply_01

//private class ApplyTest 不等于 class ApplyTest private(非常重要)
//class ApplyTest private 等于java里面用private修饰构造器
//private class ApplyTest 等于就是这个类上了锁其他地方不可见,连同一个包下面都不可见除非加上包名 private[apply_01]  class ApplyTest
 private[apply_01]  class ApplyTest  { //添加private隐藏构造器
  var age=20
  def sayHello() {
    println("hello jop")
  }
}

object ApplyTest {
  var instant: ApplyTest = null

  def apply() = {
    if (instant == null) instant = new ApplyTest
    instant

  }


}

private object ApplyDemo {
  def main(args: Array[String]) {
    //通过调用ApplyTest.apply单例 创建单例对象（伴生类加上private修饰）
    val t = ApplyTest()
    t.sayHello()
  }

}