package com.transsnet.wordcount

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCountWindows extends App {

  val sparkConf = new SparkConf().setMaster("local[2]").setAppName("WordCountWindows")

  /**
    * 创建StreamingContext需要两个参数：SparkConf和batch interval
    */
  val ssc = new StreamingContext(sparkConf, Seconds(3))
  //保存checkpoint
  ssc.checkpoint(".")

  val lines = ssc.socketTextStream("hadoop001", 9999)

  val result = lines.flatMap(_.split(" ")).map((_,1))

  //创建窗口 窗口大小和滑动大小都要是streaming产生的rdd的整数倍 窗口大小：12 12/3= 4（4个RDD来搞成一个窗口，第一次因为2个RDD来滑动，所以第一个窗口里面只有2个RDD）
  // 滑动时长 6 6/3=2 （2个RDD计算来启动一个窗口）
   val window = result.reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(12),Seconds(6))

  window.print()

  ssc.start()
  ssc.awaitTermination()


}
