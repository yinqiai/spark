package com.transsnet.implicit_01

/**
  *
  * 隐式的对类的方法进行增强，丰富现有类库的功能；
  * 隐式转换函数： 是指那种以implicit关键字声明的带有单个参数的函数
  */
class SpecialPerson(val name: String)
class Student(val name:String)
class Older(val name:String)
//参数转换 =》 类型转换例子
object Implicit01 {
  implicit def object2SpecialPerson(obj: Object): SpecialPerson ={
    if (obj.getClass == classOf[Student]){
      val stu = obj.asInstanceOf[Student];
      new SpecialPerson(stu.name)
    } else if (obj.getClass == classOf[Older]){
      val older = obj.asInstanceOf[Older];
      new SpecialPerson(older.name)
    } else new SpecialPerson(null)
    //    else Nil
  }
  var ticketNumber = 0
  def buySpecialTicket(p:SpecialPerson) = {
    ticketNumber += 1
    println("T-" + ticketNumber)
  }

  def main(args: Array[String]): Unit = {
    val student = new Student("langwei")
    //scala会自动去调用object2SpecialPerson隐式转换把student转化为SpecialPerson
    buySpecialTicket(student)
  }

}
