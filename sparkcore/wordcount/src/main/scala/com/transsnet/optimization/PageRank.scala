package com.transsnet.optimization

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author yinqi
  *
  */
object PageRank {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("PageRank").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val pgRDD = sc.textFile("C:\\Users\\11597\\Desktop\\emr\\pagerank.txt")

    val links = pgRDD.map(s => {
      val parts = s.split("\\s+")
      (parts(0), parts(1))
    }).distinct().groupByKey().cache()

    /*(CompactBuffer(i),1.0)
    (CompactBuffer(f),1.0)
    (CompactBuffer(a.2, a.1),1.0)
    (CompactBuffer(c.2, c.3, c.1),1.0)*/
    var ranks = links.mapValues(v => 1.0)
    val contribs = links.join(ranks).values
     .flatMap{case (urls, rank) =>
      val size = urls.size
      urls.map(url=>(url,rank/size))
        }
    /*(f,1.0)
    (i,1.0)
    (a.2,0.5)
    (a.1,0.5)
    (c.2,0.3333333333333333)
    (c.3,0.3333333333333333)
    (c.1,0.3333333333333333)*/
    contribs.reduceByKey(_+_).mapValues(0.15 + 0.85 * _).foreach(println)
    /*for (i <-  ranks ) {
      val contribs = links.join(ranks).values.flatMap { case (urls, rank) =>
        val size = urls.size
        urls.map(url=>(url,rank/size))
      }
      ranks = contribs.reduceByKey(_+_).mapValues(0.15 + 0.85 * _)

    }

    ranks.foreach(_ => Unit)*/
  }
}
