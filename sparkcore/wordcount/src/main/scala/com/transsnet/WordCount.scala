package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val sc =new SparkContext(new SparkConf().setAppName("workCount").setMaster("local[*]"))
    //val blankLines = sc.accumulator(0)
    val rdd1 =sc.textFile("C:\\Users\\11597\\Desktop\\big-data\\data\\data.txt")
    //map和flatMap区别
   /* a a b b
      c c d d
      e e e e
      f f*/
    val r =rdd1.flatMap(line=>
    {
      val f = line.split(" ")
      println(f.toList+"++++++++++++++++++++")
     f
    })

   /* val r =rdd1.map(line=>
    {
      val f = line.split(" ")
      println(f.toList+"++++++++++++++++++++")
      f
    })*/
        //flatMap打印写法
      println(r.collect().toList)
    //Map打印写法
     // r.collect().foreach(a=>println(a.toList))
    //println(rdd1.count())

    //println(blankLines.value)
  }
}
