package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  * @date 2020/5/14
  */
object StudySparkCore {
  def main(args: Array[String]): Unit = {
    val sparkConf =new SparkConf().setAppName("StudySparkCore").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.parallelize(Array((1,(2,3)),(4,(5,6)),(7,(8,9)),(10,(11,12)),(13,(14,15))))
    //val fbRDD =sc.textFile("C:\\Users\\11597\\Desktop\\firebase2.txt")

    //1.全局变量不行
    /*var counter = 0
    // Wrong: Don't do this!! 其行为可能会有所不同，具体取决于执行是否在同一JVM中进行。 一个常见的例子是在本地模式下运行Spark（--master = local [n]）而不是将Spark应用程序部署到集群上（例如，通过将spark-submit提交给YARN）
    rdd.foreach{case (a,(c,d)) => counter += a}

    println("Counter value: " + counter)*/
    //1.用累加器变量
   /* val acc = sc.longAccumulator("user acc")
    rdd.foreach{case (a,(c,d)) => acc.add(a)}
    println(acc.value)*/

    //2.
  }

}
