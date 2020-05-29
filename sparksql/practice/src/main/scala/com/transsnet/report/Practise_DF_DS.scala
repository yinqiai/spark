package com.transsnet.report

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types._

/**
  * @author yinqi
  * @date 2020/5/20
  */
object Practise_DF_DS {
  //1.Inferring the Schema Using Reflection
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
   /*    +-------+---+
    |   name|age|
    +-------+---+
    |Michael| 29|
      |   Andy| 30|
      | Justin| 19|
      +-------+---+*/
    val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")
    //teenagersDF.show()

    //peopleDF.map(teenager => "Name: " + {println(teenager.toString()+" ++ " +teenager.schema);teenager.getAs[String]("name")}).show()

    // The columns of a row in the result can be accessed by field index
   // peopleDF.map(teenager => {println(teenager) ;"Name: " + teenager(0)}).show()
   implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
    teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()

  }

 /* //2.Programmatically Specifying the Schema
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Practise_DF_DS").setMaster("local[*]")
    //sparkContext
    val sc = new SparkContext(sparkConf)
    //sparkSession
    val spark= SparkSession.builder().config(sc.getConf).getOrCreate()

    //这里要隐式转换一下 不然不能toDF
    val peopleRDD = spark.sparkContext
      .textFile("C:\\Users\\11597\\Desktop\\emr\\people.txt")

    // The schema is encoded in a string
    val schemaString = "name age"
    // Generate the schema based on the string of schema
    val fields = schemaString.split(" ")
      .map(fieldName => {StructField(fieldName, StringType, nullable = true)})
    val schema = StructType(fields)

    // Convert records of the RDD (people) to Rows
    val rowRDD = peopleRDD
      .map(_.split(","))
      .map(attributes => {Row(attributes(0), attributes(1).trim)})

    // Apply the schema to the RDD
    val peopleDF = spark.createDataFrame(rowRDD, schema)
    // Creates a temporary view using the DataFrame
    peopleDF.createOrReplaceTempView("people")

    // SQL can be run over a temporary view created using DataFrames
    val results = spark.sql("SELECT name FROM people")
    results.show()
    // to dataset
    // The results of SQL queries are DataFrames and support all the normal RDD operations
    // The columns of a row in the result can be accessed by field index or by field name
    import spark.implicits._
    //import  org.apache.spark.sql.SparkSession
    results.map(attributes => "Name: " + attributes(0)).show()


  }*/

  case class Person(name: String, age: Long)
}
