package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val sc =new SparkContext(new SparkConf().setAppName("workCount").setMaster("local[*]"))
    val blankLines = sc.accumulator(0)
    val rdd1 =sc.textFile("file:///C:/Users/chenlimin/Desktop/big-data/data/data.txt")
    rdd1.flatMap(line=>
    {if(line==null) blankLines+=1
      line.split(" ")})

    rdd1.collect().foreach(println)
    println(rdd1.count())

    println(blankLines.value)
  }
}
