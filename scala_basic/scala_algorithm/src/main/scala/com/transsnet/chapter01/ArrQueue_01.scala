package com.transsnet.chapter01

import scala.io.StdIn

/**
  * 数组实现单向队列
  */
object ArrQueue_01 {
  def main(args: Array[String]): Unit = {
    var key =""
    //初始化一个队列
    val queue = new QueueArr(3)
    while (true) {
      println("show 表示展示队列")
      println("exit 退出程序")
      println("add 队列里面加数据")
      println("get 队列里面取数据")
      key = StdIn.readLine()
      key match {
        case "show" => queue.showQueue()
        case "exit" => System.exit(0)
        case "add"=>{
          println("请输入一个数")
          var data= StdIn.readInt()
          queue.addQueue(data)}
        case "get"=>{
          val data=queue.getQueue()
          if(data.isInstanceOf[Exception]){
            //打印异常
           println( queue.getQueue().asInstanceOf[Exception].getMessage)
          }else{
            println(data)
          }
        }
      }
    }
  }
}

class QueueArr(maxSize: Int) {

  //1.定义一个数组存放数据
  val arr = new Array[Int](maxSize)

  //定义队列的头指针（头尾指针开始没指向任何数据）头指针是取出一个数据数值加1
  var front = -1
  //定义队列的尾指针，尾指针是加入队列一个数据数值加1
  var rear = -1

  //定义一个方法用来判断队列是否加满 就是尾指针移动到了队列最顶端
  def isFull: Boolean = {
    rear == maxSize - 1
  }

  //定义一个方法来判断队列是否为空 就是尾指针的值等于头指针的值
  def isEmpty: Boolean = {
    front == rear
  }

  //定义一个展示队列里面的元素
  def showQueue(): Any = {
    //如果队列为空 返回方法
    if (isEmpty) {
      println("队列为空，没有数据展示...")
      return
    } else {
      //遍历队列里面的数据 这里要把出队列的数据排除出来
      for (i <- front+1 to rear)
        printf("arr[%d]=%d\n", i, arr(i))
    }
  }
  //向队列加入数据
  def  addQueue(data:Int):Any={
    if(isFull){
      println("队列满了，不能加入数据了...")
      return
    }

    //rear 指针移动一步
    rear+=1
    //放入数据到队列
    arr(rear) = data

  }
  //队列取出数据
  def getQueue():Any={
    //队列没数据了 抛出异常
    if(isEmpty){
      throw new Exception("队列里面没有元素了")
    }
    // 指针移动一步
    front+=1
    return arr(front)

  }
}