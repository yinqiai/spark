package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}
//scala 算子练习
object ScalaOperator {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    sparkConf.setAppName("case_test")
    sparkConf.setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    //3是你指定的分区数，没写默认max(cpu,2)
    val rdd = sc.makeRDD(List((1,(2,3)),(2,(4,5)),(3,(5,6)),(4,(7,8))),3)
    //1.map 系列里面每个数都会调用一次
     //val rddMap = rdd.map{case (a,(b,c)) => (b,(a,c))}.collect
    //2.flatMap 系列里面每个数都会调用一次
      // val rddFlatMap = rdd.flatMap{case (a,(b,c)) =>{ println("++++++"); List(a,b,c)}}.collect
    //3.map 这里有几个分区这个函数就调用几次,注意item.map 这个map是scala集合操作中的map 而不是rdd里面的map
     // val rddMapP = rdd.mapPartitions(items => {items.map{case (a,(b,c)) =>(b,(a,c))}}).collect
    //4.mapPartitionsWithIndex 看每个分区里面都装了哪些元素
    //val rddMapPI = rdd.mapPartitionsWithIndex((a:Int,item) =>{println(a.toString+ "+++"+item.mkString("|")); item.map(b=>a.toString+"|"+b)}).collect
    //5.union 一个rdd和另外一个rdd union 注意类型要相同
     //val rddUnion = sc.makeRDD(1 to 10).union(sc.makeRDD(10 to 20))
    //6 partitionBy 重新分区,只能针对k-v结构的数据重新分区，这里面最常用的是hashparttioner 和rangeparttioner分区器
    /*val rddPartitionBy = sc
       .makeRDD(1 to 10,3)
       .map((_,2))
       .partitionBy(new org.apache.spark.HashPartitioner(5))
       .mapPartitionsWithIndex((a,item)=>item.map(b=>a.toString+"|"+b)).collect()*/
    //7.转换操作里面有shuffle 下面两个效果一样，但是reduceByKey有预聚合功能效率比groupByKey高
    //val rdd1 = sc.makeRDD(List((1,2),(1,3),(3,7),(3,1)),3)
    //val rddR = rdd1.reduceByKey((a,b)=>{println(a.toString+"*"+b.toString);a+b}).collect
    //val rddG = rdd1.groupByKey().map(a=>{println(a._2.mkString("*"));(a._1,a._2.sum)}).collect
     //8

  }

}
