package com.transsnet.spark_serialization

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  * @date 2020/5/14
  */
object SparkSerial {
  def main(args: Array[String]): Unit = {
    val sparkConf =new SparkConf().setAppName("SparkSerial").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)


    val sum = distData.map(new Operation().multiply).sum()

    //val sum = distData.map(x => x * 2).sum()
    println(sum)  // 30.0
    sc.stop()
  }

}

class Operation {
  val n = 2
  //def multiply = (x: Int) => x * n

  def multiply = (x: Int) => x * 2
}
