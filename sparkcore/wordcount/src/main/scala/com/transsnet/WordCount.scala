package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val sc =new SparkContext(new SparkConf().setAppName("workCount").setMaster("local[*]"))
    //val blankLines = sc.accumulator(0)
    val rdd1 =sc.textFile("C:\\Users\\11597\\Desktop\\big-data\\data\\data.txt")
    val r =rdd1.flatMap(line=>
    {
      val f = line.split(" ")
      println(f.foreach(println)+"++++++++++++++++++++")
     f
    })
        //.map(a=>(a,1))
   r.collect()
    //println(rdd1.count())

    //println(blankLines.value)
  }
}
