package com.transsnet.project

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  *
  */
object Top3 {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("workCount").setMaster("local[*]"))
    //val blankLines = sc.accumulator(0)
    val source = sc.textFile("C:\\Users\\11597\\Desktop\\big-data\\data\\agent.log")
    //1.取出需要分析的字段数据
    source.map(x => {
      val arr = x.split(" ")
      val data1 = arr(1)
      val data2 = arr(4)
      ((data1, data2), 1)
    })
      //计算省下面各个广告类型中的点击排名前三
      //.reduceByKey(_+_).map(a=>(a._1._1,(a._1._2,a._2))).groupByKey().mapValues(a=>{a.toList.sortWith((a,b)=>a._2>b._2).take(3)})
      //.coalesce(1).saveAsTextFile("C:\\Users\\11597\\Desktop\\big-data\\data\\output2\\")
      //计算省点击广告总算排名前三
      .reduceByKey(_ + _).map(a => (a._1._1, a._2)).reduceByKey(_ + _).map { case (a, b) => (b, a) }.sortByKey(false).take(3)
    sc.stop()
  }
}
