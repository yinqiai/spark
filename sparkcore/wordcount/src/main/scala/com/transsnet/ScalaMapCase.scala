package com.transsnet

import org.apache.spark.{SparkConf, SparkContext}

object ScalaMapCase {
  /**
    * map 用法深度理解
    * 1、在map中使用if-else，if分支结构最好带上else 表示把所有的情况都考虑全 否则会返回默认的空值
    * 2、在map中使用case是scala的用法和spark没有关系 同样要使用case 最好带上 case _ 表示所有的情况都考虑完全
    *     否则容易出异常
    * @param args
    */
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    sparkConf.setAppName("case_test")
    sparkConf.setMaster("local[2]")
    val sparkContext = new SparkContext(sparkConf)
    val rdd = sparkContext.textFile("C:\\Users\\chenlimin\\Desktop\\big-data\\spark\\sparkcore\\wordcount\\src\\main\\java\\com\\transsnet\\people.txt")
    rdd.map(line=>line.split(","))
      .map(line=>if(line.length == 1) (line(0)) else if(line.length == 2) (line(0),line(1)) else (line(0),line(1),line(2)))
      //这行模式匹配其实会报错的，只是idea没有报出来而已
      .map{ case (one) => ("one:"+one) case (name,age) =>("name:"+name,"age:"+age) case _ => ("_name","_age","_")}
      .foreach(println)

  }





}
