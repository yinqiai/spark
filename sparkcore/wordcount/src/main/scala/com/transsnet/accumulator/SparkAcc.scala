package com.transsnet.accumulator

import org.apache.spark.{SparkConf, SparkContext}

object SparkAcc {
  def main(args: Array[String]): Unit = {
    val sc =new SparkContext(new SparkConf().setAppName("SparkAcc").setMaster("local[*]"))
    //val blankLines = sc.accumulator(0)
    val notice  =sc.textFile("C:\\Users\\chenlimin\\Desktop\\big-data\\data\\NOTICE.txt")

    val blanklines = sc.accumulator(0)

    val tmp = notice.flatMap(line => {

               blanklines += 1

         line.split(" ")
       })

    println(tmp.count())

    println(blanklines.value)

    sc.stop()

  }

}
