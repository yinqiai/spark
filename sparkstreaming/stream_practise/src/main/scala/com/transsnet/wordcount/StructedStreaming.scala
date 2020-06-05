package com.transsnet.wordcount

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @author yinqi
  * @date 2020/5/29
  */
object StructedStreaming {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("StructedStreaming").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val spark = SparkSession
      .builder
      .config(sc.getConf)
      .getOrCreate()

    import spark.implicits._
    // Create DataFrame representing the stream of input lines from connection to localhost:9999
    val lines = spark.readStream
      .format("socket")
      .option("host", "hadoop000")
      .option("port", 9999)
      .load()

    // Split the lines into words
    val words = lines.as[String].flatMap(_.split(" "))


    // Generate running word count
    val wordCounts = words.groupBy("value").count()

    // Start running the query that prints the running counts to the console
    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()

   val ssc= new StreamingContext(sc,Seconds(1))

    query.awaitTermination()
  }
}
