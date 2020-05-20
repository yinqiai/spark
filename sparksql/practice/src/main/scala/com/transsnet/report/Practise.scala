package com.transsnet.report

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  * @date 2020/5/20
  */
object Practise {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Practise").setMaster("local[*]")
    //sparkContext
    val sc = new SparkContext(sparkConf)
    //sparkSession
    val spark= SparkSession.builder().config(sc.getConf).getOrCreate()

    //这里要隐式转换一下 不然不能toDF
    import spark.implicits._
    val peopleDF = spark.sparkContext
      .textFile("C:\\Users\\11597\\Desktop\\emr\\people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDF()
    // Register the DataFrame as a temporary view
    peopleDF.createOrReplaceTempView("people")

    // SQL statements can be run by using the sql methods provided by Spark
    val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")

    // The columns of a row in the result can be accessed by field index
    teenagersDF.map(teenager => "Name: " + teenager(0)).show()
  }

  case class Person(name: String, age: Long)
}
