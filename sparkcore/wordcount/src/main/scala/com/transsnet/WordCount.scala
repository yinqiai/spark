package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val sc =new SparkContext(new SparkConf().setAppName("workCount").setMaster("local[*]"))
    //val blankLines = sc.accumulator(0)
    val rdd1 =sc.textFile("C:\\Users\\11597\\Desktop\\big-data\\data\\test-partition\\")
//2.测试mapPartitions
  /* val mapP= rdd1.flatMap(_.split(" ")).map((_,1)).mapPartitions(x=>
     {
       //println(x.toList) //这步骤加了，后面map代码就不执行了
     x.map(x=>(x._1,x._2*2))//scala里面的map
     }).collect()

    mapP.foreach(println)*/

  //3。distinct
 /* val distinctRdd = sc.parallelize(List(1,2,1,5,2,9,6,1))
    val unionRDD = distinctRdd.distinct(2)*/


    //4.aggregateByKey

/*    val aggregateRdd = sc.parallelize(List(("a",3)("a",2),("c",4),("b",3),("c",6),("c",8)),4)
    aggregateRdd.aggregateByKey(0)((a,b)=>if (a>b) a else b,_+_)*/

   // 5.combineByKey
   /* val combineRdd = sc.parallelize(Array(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b",98)),3)
      .combineByKey((_,1),(a:(Int,Int),b)=>(a._1+b,a._2+1),(a:(Int,Int),b:(Int,Int))=>(a._1+b._1,a._2+b._2))
      //.map(x=>{ x match {case(k,v) =>(k,v._1/v._2)}}) 写法1
     // .map{case(k,v) =>(k,v._1/v._2)} 写法二 是写法1的简写
      //.map(x=>{ (x._1,x._2._1/x._2._2)}) 写法三 是最普通实现方式
      .saveAsTextFile("output_combine1")*/

    //6.sortByKey
    val combineRdd = sc.parallelize(Array((88,"a" ), (95,"b" ), (91,"a" ), (93,"b" ), (95,"a"), (98,"b")),3).sortByKey().saveAsTextFile("output_sort")
    //map和flatMap区别
   /* a a b b
      c c d d
      e e e e
      f f*/
   /* val r =rdd1.flatMap(line=>
    {
      val f = line.split(" ")
      println(f.toList+"++++++++++++++++++++")
     f
    })*/

   /* val r =rdd1.map(line=>
    {
      val f = line.split(" ")
      println(f.toList+"++++++++++++++++++++")
      f
    })*/



        //flatMap打印写法
      //println(r.collect().toList)
    //Map打印写法
     // r.collect().foreach(a=>println(a.toList))
    //println(rdd1.count())

    //println(blankLines.value)
  }
}
